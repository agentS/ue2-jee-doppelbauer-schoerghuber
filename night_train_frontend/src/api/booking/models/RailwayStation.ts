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
/**
 * 
 * @export
 * @interface RailwayStation
 */
export interface RailwayStation {
    /**
     * 
     * @type {number}
     * @memberof RailwayStation
     */
    id?: number;
    /**
     * 
     * @type {string}
     * @memberof RailwayStation
     */
    name?: string;
}

export function RailwayStationFromJSON(json: any): RailwayStation {
    return RailwayStationFromJSONTyped(json, false);
}

export function RailwayStationFromJSONTyped(json: any, ignoreDiscriminator: boolean): RailwayStation {
    if ((json === undefined) || (json === null)) {
        return json;
    }
    return {
        
        'id': !exists(json, 'id') ? undefined : json['id'],
        'name': !exists(json, 'name') ? undefined : json['name'],
    };
}

export function RailwayStationToJSON(value?: RailwayStation | null): any {
    if (value === undefined) {
        return undefined;
    }
    if (value === null) {
        return null;
    }
    return {
        
        'id': value.id,
        'name': value.name,
    };
}

