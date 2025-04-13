package com.mediscan.ai.domain

interface RequestHandler<BODY: RequestHandler.Request,RESPONSE: RequestHandler.Response> {

    fun handleRequest(body: BODY): RESPONSE

    interface Request

    interface Response

}