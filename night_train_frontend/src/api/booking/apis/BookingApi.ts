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


import * as runtime from '../runtime';
import {
    BookingDto,
    BookingDtoFromJSON,
    BookingDtoToJSON,
    BookingRequestDto2,
    BookingRequestDto2FromJSON,
    BookingRequestDto2ToJSON,
    BookingResponseDto,
    BookingResponseDtoFromJSON,
    BookingResponseDtoToJSON,
} from '../models';

export interface ApiBookingsIdGetRequest {
    id: number;
}

export interface ApiBookingsPostRequest {
    bookingRequestDto2?: BookingRequestDto2;
}

/**
 * no description
 */
export class BookingApi extends runtime.BaseAPI {

    /**
     */
    async apiBookingsIdGetRaw(requestParameters: ApiBookingsIdGetRequest): Promise<runtime.ApiResponse<BookingDto>> {
        if (requestParameters.id === null || requestParameters.id === undefined) {
            throw new runtime.RequiredError('id','Required parameter requestParameters.id was null or undefined when calling apiBookingsIdGet.');
        }

        const queryParameters: runtime.HTTPQuery = {};

        const headerParameters: runtime.HTTPHeaders = {};

        const response = await this.request({
            path: `/api/bookings/{id}`.replace(`{${"id"}}`, encodeURIComponent(String(requestParameters.id))),
            method: 'GET',
            headers: headerParameters,
            query: queryParameters,
        });

        return new runtime.JSONApiResponse(response, (jsonValue) => BookingDtoFromJSON(jsonValue));
    }

    /**
     */
    async apiBookingsIdGet(requestParameters: ApiBookingsIdGetRequest): Promise<BookingDto> {
        const response = await this.apiBookingsIdGetRaw(requestParameters);
        return await response.value();
    }

    /**
     */
    async apiBookingsPostRaw(requestParameters: ApiBookingsPostRequest): Promise<runtime.ApiResponse<BookingResponseDto>> {
        const queryParameters: runtime.HTTPQuery = {};

        const headerParameters: runtime.HTTPHeaders = {};

        headerParameters['Content-Type'] = 'application/json';

        const response = await this.request({
            path: `/api/bookings`,
            method: 'POST',
            headers: headerParameters,
            query: queryParameters,
            body: BookingRequestDto2ToJSON(requestParameters.bookingRequestDto2),
        });

        return new runtime.JSONApiResponse(response, (jsonValue) => BookingResponseDtoFromJSON(jsonValue));
    }

    /**
     */
    async apiBookingsPost(requestParameters: ApiBookingsPostRequest): Promise<BookingResponseDto> {
        const response = await this.apiBookingsPostRaw(requestParameters);
        return await response.value();
    }

}
