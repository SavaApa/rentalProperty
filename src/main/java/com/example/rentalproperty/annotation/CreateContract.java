package com.example.rentalproperty.annotation;

import com.example.rentalproperty.entity.Contract;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.core.annotation.AliasFor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@RequestMapping(method = RequestMethod.POST)
@Operation(
        summary = "Create a new contract",
        description = "Creation of a new contract and return",
        tags = {"CONTRACT"},
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "The contract to be created",
                required = true,
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = Contract.class)
                )
        ),
        responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "The contract created",
                        content = @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = Contract.class)
                        )
                ),
                @ApiResponse(
                        responseCode = "400",
                        description = "The contract already exist",
                        content = @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = Contract.class)
                        )
                )
        }
)
public @interface CreateContract {
    @AliasFor(annotation = RequestMapping.class, attribute = "path")
    String[] path() default {};
}
