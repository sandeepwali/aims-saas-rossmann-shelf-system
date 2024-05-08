package com.solumesl.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.solumesl.entity.Shelf;
import com.solumesl.entity.Shelfitem;
import com.solumesl.exception.MessageDTO;
import com.solumesl.exception.NotFoundException;
import com.solumesl.repository.ShelfRepository;
import com.solumesl.repository.ShelfitemRepository;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;

@OpenAPIDefinition(info = @Info(
        title = "SoluM ESL Shelf Item System REST API",
        version = "0.1", description = "RESTful API for ESL Shelf Item Article Positions"
), security = @SecurityRequirement(name = "api-key"))
@SecurityScheme(type = SecuritySchemeType.APIKEY, in = SecuritySchemeIn.HEADER, name = "api-key")
@RestController
@RequestMapping(path = "/api/v1/")
@Tag(name = "Shelf Item Controller", description = "RESTful API for ESL Shelf Item Article Positions")
@Validated
public class ShelfitemController {
	/**
	 * 
	 * @author sandeep
	 *
	 */
	private Logger LOG = LoggerFactory.getLogger(ShelfitemController.class);
	
	@Autowired
	private ShelfitemRepository shelfitemRepository;
	
	@Autowired
	private ShelfRepository shelfRepository;
	
	
	
    @SuppressWarnings("unchecked")
	@Operation(summary = "Returns article items based on the database id")
    @ApiResponses(value = {
	    @ApiResponse(responseCode = "200", description = "OK",
	    content = @Content),
	    @ApiResponse(responseCode = "400", description = "Bad Request",
	    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = MessageDTO.class))}),
	    @ApiResponse(responseCode = "401", description = "Unauthorized", 
		content = @Content),
	    @ApiResponse(responseCode = "403", description = "Forbidden",
	    content = @Content),
	    @ApiResponse(responseCode = "404", description = "Not Found",
	    content = @Content)
    })
    @GetMapping("/get_shelf/{shelfId}/shelfitem")
	public List<Shelfitem> getContactByShelfId(@PathVariable Long shelfId) {
		this.LOG.info("GET /get_shelf/{shelfId}/shelfitem with shelfId " + shelfId);
		if (!this.shelfRepository.existsById(shelfId)) {
			throw new NotFoundException(
					"Shelf not found for id " + shelfId + ". Try to get the id with GET /get_shelf .");
		} else {
			return this.shelfitemRepository.findByShelfId(shelfId);
		}
	}

    @SuppressWarnings("unchecked")
	@Operation(summary = "Returns all shelf row information for an article")
    @ApiResponses(value = {
	    @ApiResponse(responseCode = "200", description = "OK",
	    content = @Content),
	    @ApiResponse(responseCode = "400", description = "Bad Request",
	    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = MessageDTO.class))}),
	    @ApiResponse(responseCode = "401", description = "Unauthorized", 
		content = @Content),
	    @ApiResponse(responseCode = "403", description = "Forbidden",
	    content = @Content),
	    @ApiResponse(responseCode = "404", description = "Not Found",
	    content = @Content)
    })
    @GetMapping("/get_article/{articleno}")
	public List<Shelfitem> getAllshelfByArticle(@PathVariable String articleno) {
		this.LOG.info("GET /get_article by article_no " + articleno);
		return this.shelfitemRepository.findByArticleno(articleno);
	}

    @SuppressWarnings("unchecked")
	@Operation(summary = "Add new article item to the shelf row")
    @ApiResponses(value = {
	    @ApiResponse(responseCode = "200", description = "OK",
	    content = @Content),
	    @ApiResponse(responseCode = "400", description = "Bad Request",
	    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = MessageDTO.class))}),
	    @ApiResponse(responseCode = "401", description = "Unauthorized", 
		content = @Content),
	    @ApiResponse(responseCode = "403", description = "Forbidden",
	    content = @Content),
	    @ApiResponse(responseCode = "404", description = "Not Found",
	    content = @Content)
    })
    @PostMapping("/post_shelf/{shelfId}/shelfitem")
	public Shelfitem addShelfitem(@PathVariable Long shelfId, @RequestBody @Valid Shelfitem shelfitem) {
		this.LOG.info("POST /post_shelf/{shelfId}/shelfitem with shelfId " + shelfId);
		return (Shelfitem) this.shelfRepository.findById(shelfId).map((shelf) -> {
			shelfitem.setShelf(shelf);
			return (Shelfitem) this.shelfitemRepository.save(shelfitem);
		}).orElseThrow(() -> {
			return new NotFoundException(
					"Shelf not found for id " + shelfId + ". Try to get the id with GET /get_shelf .");
		});
	}

    @SuppressWarnings("unchecked")
	@Operation(summary = "Add new article item list to the shelf row")
    @ApiResponses(value = {
	    @ApiResponse(responseCode = "200", description = "OK",
	    content = @Content),
	    @ApiResponse(responseCode = "400", description = "Bad Request",
	    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = MessageDTO.class))}),
	    @ApiResponse(responseCode = "401", description = "Unauthorized", 
		content = @Content),
	    @ApiResponse(responseCode = "403", description = "Forbidden",
	    content = @Content),
	    @ApiResponse(responseCode = "404", description = "Not Found",
	    content = @Content)
    })
    @PostMapping("/post_shelf/{shelfId}/shelfitem_list")
	public List<Shelfitem> addShelfitemList(@PathVariable Long shelfId, @RequestBody @Valid List<Shelfitem> shelfitem) {
		this.LOG.info("POST /post_shelf/{shelfId}/shelfitem with shelfId " + shelfId);
		Optional<Shelf> shelf = this.shelfRepository.findById(shelfId);
		if (!shelf.isPresent()) {
			throw new NotFoundException(
					"Shelf not found for id " + shelfId + ". Try to get the id with GET /get_shelf .");
		} else {
			Shelf tmp = (Shelf) shelf.get();

			for (int a = 0; a < shelfitem.size(); ++a) {
				((Shelfitem) shelfitem.get(a)).setShelf(tmp);
			}

			return this.shelfitemRepository.saveAll(shelfitem);
		}
	}

    @SuppressWarnings("unchecked")
	@Operation(summary = "Change article items in a shelf row")
    @ApiResponses(value = {
	    @ApiResponse(responseCode = "200", description = "OK",
	    content = @Content),
	    @ApiResponse(responseCode = "400", description = "Bad Request",
	    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = MessageDTO.class))}),
	    @ApiResponse(responseCode = "401", description = "Unauthorized", 
		content = @Content),
	    @ApiResponse(responseCode = "403", description = "Forbidden",
	    content = @Content),
	    @ApiResponse(responseCode = "404", description = "Not Found",
	    content = @Content)
    })
    @PutMapping("/put_shelf/{shelfId}/shelfitem/{shelfitemId}")
	public Shelfitem updateShelfitem(@PathVariable Long shelfId, @PathVariable Long shelfitemId,
			@RequestBody @Valid Shelfitem shelfitemUpdated) {
		this.LOG.info("PUT /put_shelf/{shelfId}/shelfitem/{shelfitemId} with shelfId " + shelfId + " and shelfitemId "
				+ shelfitemId);
		if (!this.shelfRepository.existsById(shelfId)) {
			throw new NotFoundException(
					"Shelf not found for id " + shelfId + ". Try to get the id with GET /get_shelf .");
		} else {
			return (Shelfitem) this.shelfitemRepository.findById(shelfitemId).map((shelfitem) -> {
				if (shelfitemUpdated.getOrderno() != null) {
					shelfitem.setOrderno(shelfitemUpdated.getOrderno());   
					
				}

				if (shelfitemUpdated.getArticleno() != null) {
					shelfitem.setArticleno(shelfitemUpdated.getArticleno());
				}

				if (shelfitemUpdated.getLength() != null) {
					shelfitem.setLength(shelfitemUpdated.getLength());
				}

				return (Shelfitem) this.shelfitemRepository.save(shelfitem);
			}).orElseThrow(() -> {
				return new NotFoundException("Shelf item not found with id " + shelfitemId
						+ ". Try to get the id with GET /get_shelf/{shelfId}/shelfitem .");
			});
		}
	}

    @SuppressWarnings("unchecked")
	@Operation(summary = "Remove a shelf row item")
    @ApiResponses(value = {
	    @ApiResponse(responseCode = "200", description = "OK",
	    content = @Content),
	    @ApiResponse(responseCode = "400", description = "Bad Request",
	    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = MessageDTO.class))}),
	    @ApiResponse(responseCode = "401", description = "Unauthorized", 
		content = @Content),
	    @ApiResponse(responseCode = "403", description = "Forbidden",
	    content = @Content),
	    @ApiResponse(responseCode = "404", description = "Not Found",
	    content = @Content)
    })
    @DeleteMapping("/remove_shelf/{shelfId}/shelfitem/{shelfitemId}")
	public String deleteShelfitem(@PathVariable Long shelfId, @PathVariable Long shelfitemId) {
		this.LOG.info("PUT /remove_shelf/{shelfId}/shelfitem/{shelfitemId} with shelfId " + shelfId
				+ " and shelfitemId " + shelfitemId);
		if (!this.shelfRepository.existsById(shelfId)) {
			throw new NotFoundException(
					"Shelf not found for id " + shelfId + ". Try to get the id with GET /get_shelf .");
		} else {
			return (String) this.shelfitemRepository.findById(shelfitemId).map((shelfitem) -> {
				this.shelfitemRepository.delete(shelfitem);
				return "Deleted Successfully!";
			}).orElseThrow(() -> {
				return new NotFoundException("Shelf item not found for id " + shelfitemId
						+ ". Try to get the id's with GET /get_shelf/{shelfId}/shelfitem .");
			});
		}
	}
	
	
}
