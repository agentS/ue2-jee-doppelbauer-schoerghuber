import React from "react";
import { Route } from "react-router-dom";

import { Navbar, Nav } from "react-bootstrap";
import "bootstrap/dist/css/bootstrap.min.css";

import { TimetableApi } from "./api/timetable";

import ConnectionSearch from "./timetable/ConnectionSearch";
import DiscoverySearch from "./timetable/DiscoverySearch";

class App extends React.Component {
	render() {
		const timetableApi = new TimetableApi();

		const connectionSearch = () => (<ConnectionSearch timetableApi={timetableApi}/>);
		const discoverySearch = () => (<DiscoverySearch timetableApi={timetableApi}/>);
		const discovery = ({match}: any) => {
			return (<h1>{match.params.departureStationId}</h1>)
		};

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
			</div>
		);
	}
}

export default App;
