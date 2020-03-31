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
    StopDto,
    StopDtoFromJSON,
    StopDtoFromJSONTyped,
    StopDtoToJSON,
} from './';

/**
 * 
 * @export
 * @interface TicketDto
 */
export interface TicketDto {
    /**
     * 
     * @type {number}
     * @memberof TicketDto
     */
    destinationId?: number;
    /**
     * 
     * @type {number}
     * @memberof TicketDto
     */
    id?: number;
    /**
     * 
     * @type {number}
     * @memberof TicketDto
     */
    originId?: number;
    /**
     * 
     * @type {Array<StopDto>}
     * @memberof TicketDto
     */
    stops?: Array<StopDto>;
    /**
     * 
     * @type {string}
     * @memberof TicketDto
     */
    trainCode?: string;
}

export function TicketDtoFromJSON(json: any): TicketDto {
    return TicketDtoFromJSONTyped(json, false);
}

export function TicketDtoFromJSONTyped(json: any, ignoreDiscriminator: boolean): TicketDto {
    if ((json === undefined) || (json === null)) {
        return json;
    }
    return {
        
        'destinationId': !exists(json, 'destinationId') ? undefined : json['destinationId'],
        'id': !exists(json, 'id') ? undefined : json['id'],
        'originId': !exists(json, 'originId') ? undefined : json['originId'],
        'stops': !exists(json, 'stops') ? undefined : ((json['stops'] as Array<any>).map(StopDtoFromJSON)),
        'trainCode': !exists(json, 'trainCode') ? undefined : json['trainCode'],
    };
}

export function TicketDtoToJSON(value?: TicketDto | null): any {
    if (value === undefined) {
        return undefined;
    }
    if (value === null) {
        return null;
    }
    return {
        
        'destinationId': value.destinationId,
        'id': value.id,
        'originId': value.originId,
        'stops': value.stops === undefined ? undefined : ((value.stops as Array<any>).map(StopDtoToJSON)),
        'trainCode': value.trainCode,
    };
}


