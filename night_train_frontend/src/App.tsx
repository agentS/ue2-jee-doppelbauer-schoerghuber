import React from "react";
import { Route } from "react-router-dom";

import { Navbar, Nav } from "react-bootstrap";
import "bootstrap/dist/css/bootstrap.min.css";

import { TimetableApi } from "./api/timetable";
import { BookingApi } from "./api/booking";

import ConnectionSearch from "./timetable/ConnectionSearch";
import DiscoverySearch from "./timetable/DiscoverySearch";
import Discovery from "./timetable/Discovery";
import ConnectionDisplay from "./timetable/ConnectionDisplay";
import BookingDisplay from "./booking/BookingDisplay";

class App extends React.Component {
	render() {
		const timetableApi = new TimetableApi();
		const bookingApi = new BookingApi();

		const connectionSearch = () => (<ConnectionSearch timetableApi={timetableApi}/>);
		const discoverySearch = () => (<DiscoverySearch timetableApi={timetableApi}/>);
		const discovery = ({match}: any) => (<Discovery
			departureStationId={match.params.departureStationId}
			timetableApi={timetableApi}/>
		);
		const connectionDisplay = ({match}: any) => (<ConnectionDisplay
			departureStationId={match.params.departureStationId}
			arrivalStationId={match.params.arrivalStationId}
			timetableApi={timetableApi}
			bookingApi={bookingApi}/>
		);
		const bookingDisplay = ({match}: any) => (<BookingDisplay
			bookingId={match.params.bookingId}
			bookingApi={bookingApi}/>
		);

		return (
			<div className="container">
				<Navbar bg="light" expand="lg">
					<Navbar.Brand href="/">Night Train Booking System</Navbar.Brand>
					<Navbar.Toggle aria-controls="navigationBar"/>
					<Navbar.Collapse id="navigationBar">
						<Nav className="mr-auto">
							<Nav.Link href="/connectionSearch">Connection Search</Nav.Link>
							<Nav.Link href="/discoverySearch">Discovery</Nav.Link>
						</Nav>
					</Navbar.Collapse>
				</Navbar>

				<Route exact path="/" component={connectionSearch}/>
				<Route exact path="/connectionSearch" component={connectionSearch}/>
				<Route exact path="/discoverySearch" component={discoverySearch}/>
				<Route exact path="/discovery/:departureStationId" component={discovery}/>
				<Route exact path="/connection/from/:departureStationId/to/:arrivalStationId" component={connectionDisplay}/>
				<Route exact path="/booking/:bookingId" component={bookingDisplay}/>
			</div>
		);
	}
}

export default App;
