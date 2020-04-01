import React from "react";
import { Table, Accordion, Card, Button } from "react-bootstrap";

import moment from "moment";

import { MOMENT_DISPLAY_DATE_FORMAT } from "../Constants";

import { BookingApi, BookingDto, TicketDto } from "../api/booking";

interface BookingDisplayProperties {
	bookingApi: BookingApi;
	bookingId: number;
}

interface BookingDisplayState {
	booking?: BookingDto
}

class BookingDisplay extends React.Component<BookingDisplayProperties, BookingDisplayState> {
	constructor(properties: BookingDisplayProperties) {
		super(properties);
		this.state = {
			booking: undefined
		};
	}

	async componentDidMount() {
		await this.loadBooking();
	}

	async componentDidUpdate(previousProperties: BookingDisplayProperties) {
		if (this.props.bookingId !== previousProperties.bookingId) {
			await this.loadBooking();
		}
	}

	async loadBooking() {
		const booking = await this.props.bookingApi.apiBookingsIdGet({ id: this.props.bookingId });
		this.setState({ booking });
	}

	render() {
		if ((this.state.booking !== undefined) && (this.state.booking !== null)) {
			return (
				<div className="mt-4">
					<h1>{this.state.booking.originStationName} to {this.state.booking.destinationStationName}</h1>
					<h1>Departure date: {moment(this.state.booking.departureDate).format(MOMENT_DISPLAY_DATE_FORMAT)}</h1>
					<h2>Tickets</h2>
					<Accordion>
						{this.state.booking.tickets?.map(ticket => (
							<Card key={ticket.id}>
								<Card.Header>
									<Accordion.Toggle as={Button} variant="link" eventKey={convertTicketIdToString(ticket)}>
										{ticket.trainCode}
									</Accordion.Toggle>
								</Card.Header>
								<Accordion.Collapse eventKey={convertTicketIdToString(ticket)}>
									<Card.Body>
										<h3>Train code: {ticket.trainCode}</h3>

										<h5>Reservation</h5>
										<p>Car type: {ticket.stops?.[0].reservation?.trainCar?.type}</p>
										<p>Car number: {ticket.stops?.[0].reservation?.trainCar?.number}</p>

										<h5>Timetable</h5>
										<Table striped bordered>
											<thead>
												<tr>
													<th>Departure stop name</th>
													<th>Departure time</th>
													<th>Arrival stop name</th>
													<th>Arrival time</th>
												</tr>
											</thead>
											<tbody>
												{ticket.stops?.map(stop => (
													<tr key={`${stop.connection?.departureStation?.id}_${stop.connection?.arrivalStation?.id}`}>
														<td>{stop.connection?.departureStation?.name}</td>
														<td>{stop.connection?.departureTime}</td>
														<td>{stop.connection?.arrivalStation?.name}</td>
														<td>{stop.connection?.arrivalTime}</td>
													</tr>
												))}
											</tbody>
										</Table>
									</Card.Body>
								</Accordion.Collapse>
							</Card>
						))}
					</Accordion>
				</div>
			);
		} else {
			return (<h3>Loading...</h3>);
		}
		
	}
}

function convertTicketIdToString(ticket: TicketDto): string {
	if ((ticket.id !== undefined) && (ticket.id !== null)) {
		return ticket.id.toString();
	} else {
		throw new Error("Undefined ticket ID");
	}
}

export default BookingDisplay;
