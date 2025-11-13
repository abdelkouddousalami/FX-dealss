package com.bloomberg.fxdeals.controller;

import com.bloomberg.fxdeals.dto.FxDealRequest;
import com.bloomberg.fxdeals.dto.FxDealResponse;
import com.bloomberg.fxdeals.service.FxDealService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Path("/api/fx-deals")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class FxDealController {

    private static final Logger logger = LoggerFactory.getLogger(FxDealController.class);

    @Inject
    private FxDealService dealService;

    @POST
    public Response createDeal(FxDealRequest request) {
        logger.info("Received request to create FX deal");
        FxDealResponse response = dealService.createDeal(request);
        return Response.status(Response.Status.CREATED)
            .entity(response)
            .build();
    }

    @GET
    @Path("/{id}")
    public Response getDealById(@PathParam("id") Long id) {
        logger.info("Received request to get FX deal by ID: {}", id);
        FxDealResponse response = dealService.getDealById(id);
        return Response.ok(response).build();
    }

    @GET
    @Path("/unique/{dealUniqueId}")
    public Response getDealByUniqueId(@PathParam("dealUniqueId") String dealUniqueId) {
        logger.info("Received request to get FX deal by unique ID: {}", dealUniqueId);
        FxDealResponse response = dealService.getDealByUniqueId(dealUniqueId);
        return Response.ok(response).build();
    }

    @GET
    public Response getAllDeals(
            @QueryParam("offset") @DefaultValue("0") int offset,
            @QueryParam("limit") @DefaultValue("100") int limit) {
        logger.info("Received request to get all FX deals with offset: {} and limit: {}", offset, limit);
        
        if (limit <= 0 || limit > 1000) {
            limit = 100;
        }
        if (offset < 0) {
            offset = 0;
        }

        List<FxDealResponse> deals = dealService.getDeals(offset, limit);
        long totalCount = dealService.getTotalCount();

        return Response.ok()
            .entity(deals)
            .header("X-Total-Count", totalCount)
            .header("X-Offset", offset)
            .header("X-Limit", limit)
            .build();
    }

    @GET
    @Path("/health")
    @Produces(MediaType.TEXT_PLAIN)
    public Response health() {
        return Response.ok("FX Deals API is running").build();
    }
}
