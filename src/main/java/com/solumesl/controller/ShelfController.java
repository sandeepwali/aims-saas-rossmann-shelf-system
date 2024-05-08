package com.solumesl.controller;


import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.solumesl.entity.Shelf;
import com.solumesl.exception.MessageDTO;
import com.solumesl.exception.NotFoundException;
import com.solumesl.repository.ShelfRepository;

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
        title = "SoluM ESL Shelf System REST API",
        version = "0.1", description = "RESTful API for ESL Shelf Article Positions"
), security = @SecurityRequirement(name = "api-key"))
@SecurityScheme(type = SecuritySchemeType.APIKEY, in = SecuritySchemeIn.HEADER, name = "api-key")
@RestController
@RequestMapping(path = "/api/v1/")
@Tag(name = "Shelf Controller", description = "RESTful API for ESL Shelf Article Positions")
@Validated
public class ShelfController {
	/**
	 * 
	 * @author sandeep
	 *
	 */

	private Logger LOG = LoggerFactory.getLogger(ShelfController.class);
	
	@Autowired
	private ShelfRepository shelfRepository;

    @SuppressWarnings("unchecked")
	@Operation(summary = "Returns article items based on module, shelf and row")
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
    @GetMapping("/get_shelf")
	public List<Shelf> getShelf(@RequestParam(required = true) String store,
								@RequestParam(required = true) String module,
								@RequestParam(required = true) String shelf, 
								@RequestParam(required = true) Integer rowno) 
    {
		this.LOG.info("GET /get_shelf with Store "+ store + ", module "  + module + ", Shelf " + shelf + ",  Row No. " + rowno);
		List<Shelf> optShelf = this.shelfRepository.findByStoreAndModuleAndShelfAndRowno(store,module, shelf, rowno);
		if (optShelf.isEmpty()) {
			throw new NotFoundException("Shelf not found with Store "+ store + ", module " + module + ", Shelf " + shelf + ",  Row No. " + rowno);
		} else {
			return optShelf;
		}
	}

    @SuppressWarnings("unchecked")
	@Operation(summary = "Returns all shelf row information as list with articleno filter")
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
    @GetMapping("/get_shelf_list_article/{articleno}")
	public List<Shelf> getAllshelfByArticle(@PathVariable String articleno) {
		this.LOG.info("GET /get_shelf_list_article/{articleno}" + articleno);
		List<Shelf> optShelf = this.shelfRepository.findAllByArticleno(articleno);
		if (optShelf.isEmpty()) {
			throw new NotFoundException("Shelf not found with Article No. " + articleno
					+ " You may try to check for available articleno's with GET/api/v1/get_shelf ");
		} else {
			return optShelf;
		}
	}

	private Sort sortByIdAsc() {
	    return Sort.by(Direction.DESC, "id");
	}

    @SuppressWarnings("unchecked")
	@Operation(summary = "Returns all shelf row information as list")
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
    @GetMapping("/get_shelf_list")
    public List<Shelf> getAllshelf() {
		this.LOG.info("GET /shelf");
		return this.shelfRepository.findAll(this.sortByIdAsc());
	}

    @SuppressWarnings("unchecked")
	@Operation(summary = "Returns shelf information based on the id")
    @ApiResponses(value = {
	    @ApiResponse(responseCode = "200", description = "OK",
	    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Shelf.class))}),
	    @ApiResponse(responseCode = "400", description = "Bad Request",
	    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = MessageDTO.class))}),
	    @ApiResponse(responseCode = "401", description = "Unauthorized", 
		content = @Content),
	    @ApiResponse(responseCode = "403", description = "Forbidden",
	    content = @Content),
	    @ApiResponse(responseCode = "404", description = "Not Found",
	    content = @Content)
    })
    @GetMapping("/get_shelf/{id}")
	public Shelf getShelfByID(@PathVariable Long id) {
		this.LOG.info("GET /get_shelf/{id} id: " + id);
		Optional<Shelf> optShelf = this.shelfRepository.findById(id);
		if (optShelf.isPresent()) {
			return (Shelf) optShelf.get();
		} else {
			throw new NotFoundException("Shelf not found with id " + id + ". Try to get the id with GET /get_shelf .");
		}
	}

    @SuppressWarnings("unchecked")
	@Operation(summary = "Enter a new shelf row")
    @ApiResponses(value = {
	    @ApiResponse(responseCode = "200", description = "OK",
	    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Shelf.class))}),
	    @ApiResponse(responseCode = "400", description = "Bad Request",
	    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = MessageDTO.class))}),
	    @ApiResponse(responseCode = "401", description = "Unauthorized", 
		content = @Content),
	    @ApiResponse(responseCode = "403", description = "Forbidden",
	    content = @Content),
	    @ApiResponse(responseCode = "404", description = "Not Found",
	    content = @Content)
    })
    @PostMapping(value = "/post_shelf", consumes = "application/json")
	public Shelf createShelf(@RequestBody @Valid Shelf shelf) {
		this.LOG.info("POST /post_shelf/" + shelf.toString());
		return (Shelf) this.shelfRepository.save(shelf);
	}

    
    @SuppressWarnings("unchecked")
	@Operation(summary = "Enter new shelf rows as list")
    @ApiResponses(value = {
	    @ApiResponse(responseCode = "200", description = "OK",
	    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Shelf.class))}),
	    @ApiResponse(responseCode = "400", description = "Bad Request",
	    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = MessageDTO.class))}),
	    @ApiResponse(responseCode = "401", description = "Unauthorized", 
		content = @Content),
	    @ApiResponse(responseCode = "403", description = "Forbidden",
	    content = @Content),
	    @ApiResponse(responseCode = "404", description = "Not Found",
	    content = @Content)
    })
    @PostMapping(value = "/post_shelf_list", consumes = "application/json")
	public List<Shelf> createShelfList(@RequestBody @Valid List<Shelf> shelfs) {
		this.LOG.info("POST /post_shelf_list/" + shelfs.toString());
		return this.shelfRepository.saveAll(shelfs);
	}

    @SuppressWarnings("unchecked")
	@Operation(summary = "Update an existing shelf row based on id")
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
    @PutMapping(value = "/put_shelf/{id}", consumes = "application/json")
	public Shelf updateShelf(@PathVariable Long id, @RequestBody @Valid Shelf shelfUpdated) {
		this.LOG.info("PUT /put_shelf/");
		return (Shelf) this.shelfRepository.findById(id).map((shelf) -> {
			if (shelfUpdated.getStore() != null) {
				shelf.setStore(shelfUpdated.getStore());
			}
			
			if (shelfUpdated.getModule() != null) {
				shelf.setModule(shelfUpdated.getModule());
			}

			if (shelfUpdated.getShelf() != null) {
				shelf.setShelf(shelfUpdated.getShelf());
			}

			if (shelfUpdated.getRowno() != null) {
				shelf.setRowno(shelfUpdated.getRowno());
			}

			return (Shelf) this.shelfRepository.save(shelf);
		}).orElseThrow(() -> {
			return new NotFoundException(
					"Shelf not found with database id " + id + ". Try to get the id with GET /get_shelf .");
		});
	}

    @SuppressWarnings("unchecked")
	@Operation(summary = "Remove an existing shelf row based on id")
    @ApiResponses(value = {
	    @ApiResponse(responseCode = "200", description = "Delete Successfully!",
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
    @DeleteMapping(value = "/remove_shelf/{id}")
	public String deleteShelf(@PathVariable Long id) {
		this.LOG.info("DELETE /remove_shelf/{id} with id " + id);
		return (String) this.shelfRepository.findById(id).map((shelf) -> {
			this.shelfRepository.delete(shelf);
			return "Delete Successfully!";
		}).orElseThrow(() -> {
			return new NotFoundException(
					"Shelf not found with database id " + id + ". Try to get the id with GET /get_shelf .");
		});
	}

    @SuppressWarnings("unchecked")
	@Operation(summary = "Remove an existing shelf row based on module, shelf and rowno")
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
    @DeleteMapping(value = "/remove_shelf_row")
	public List<Shelf> getShelfRow( @RequestParam(required = true) String store,
									@RequestParam(required = true) String module,
									@RequestParam(required = true) String shelf, 
									@RequestParam(required = true) Integer rowno) {
		this.LOG.info("DELETE /remove_shelf_row with Store "+ store + ", module "  + module + ", Shelf " + shelf + ",  Row No. " + rowno);
		List<Shelf> optShelf = this.shelfRepository.findByStoreAndModuleAndShelfAndRowno(store,module, shelf, rowno);
		if (optShelf.isEmpty()) {
			throw new NotFoundException(
					"Shelf not found with Store "+ store + ", module "  + module + ", Shelf " + shelf + ",  Row No. " + rowno);
		} else {
			this.shelfRepository.deleteAll(optShelf);
			return optShelf;
		}
	}
}