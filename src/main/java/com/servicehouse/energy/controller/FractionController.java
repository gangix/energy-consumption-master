package com.servicehouse.energy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.servicehouse.energy.dto.FractionInfoDto;
import com.servicehouse.energy.service.FractionService;


@RestController
public class FractionController {
  
  @Autowired 
  private FractionService fractionService;

  @PostMapping(path ="/api/fractions")
  public ResponseEntity<FractionInfoDto> saveFractions(@RequestBody FractionInfoDto fractionInputRequest){ 
	  FractionInfoDto saveAll = fractionService.saveAll(fractionInputRequest);
	 
	  return ResponseEntity.ok(saveAll);
  }
  
}
