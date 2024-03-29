/* tslint:disable */
/* eslint-disable */
/**
 * Timetable API
 * No description provided (generated by Openapi Generator https://github.com/openapitools/openapi-generator)
 *
 * The version of the OpenAPI document: 0.0.0
 * 
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */

import { exists, mapValues } from '../runtime';
import {
    RailwayStationConnectionDto,
    RailwayStationConnectionDtoFromJSON,
    RailwayStationConnectionDtoFromJSONTyped,
    RailwayStationConnectionDtoToJSON,
    ReservationDto,
    ReservationDtoFromJSON,
    ReservationDtoFromJSONTyped,
    ReservationDtoToJSON,
} from './';

/**
 * 
 * @export
 * @interface StopDto
 */
export interface StopDto {
    /**
     * 
     * @type {RailwayStationConnectionDto}
     * @memberof StopDto
     */
    connection?: RailwayStationConnectionDto;
    /**
     * 
     * @type {ReservationDto}
     * @memberof StopDto
     */
    reservation?: ReservationDto;
}

export function StopDtoFromJSON(json: any): StopDto {
    return StopDtoFromJSONTyped(json, false);
}

export function StopDtoFromJSONTyped(json: any, ignoreDiscriminator: boolean): StopDto {
    if ((json === undefined) || (json === null)) {
        return json;
    }
    return {
        
        'connection': !exists(json, 'connection') ? undefined : RailwayStationConnectionDtoFromJSON(json['connection']),
        'reservation': !exists(json, 'reservation') ? undefined : ReservationDtoFromJSON(json['reservation']),
    };
}

export function StopDtoToJSON(value?: StopDto | null): any {
    if (value === undefined) {
        return undefined;
    }
    if (value === null) {
        return null;
    }
    return {
        
        'connection': RailwayStationConnectionDtoToJSON(value.connection),
        'reservation': ReservationDtoToJSON(value.reservation),
    };
}


