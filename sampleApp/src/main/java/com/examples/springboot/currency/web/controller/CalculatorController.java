package com.examples.springboot.currency.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.examples.springboot.business.CalculatorService;

/**
 * 
 * @author Prashanth Errabelli
 *
 */
@RestController
public class CalculatorController {

	private static final Logger logger = LoggerFactory.getLogger(CalculatorController.class);

	@Autowired
	CalculatorService service;

	@RequestMapping(value = "/calculator/add/{a}/{b}", method = {
			RequestMethod.GET }, produces = "application/hal+json")
	public ResponseEntity<Integer> add(@PathVariable int a, @PathVariable int b, HttpServletRequest request) {

		logger.debug("starts");

		logger.info("Example CalculatorProcessor (/add) ->  a :{}, b:{}", a, b);

		logger.debug("ends");

		return new ResponseEntity<Integer>(service.add(a, b), HttpStatus.OK);
	}

	@RequestMapping(value = "/calculator/multiply/{a}/{b}", method = {
			RequestMethod.GET }, produces = "application/hal+json")
	public ResponseEntity<Integer> multiply(@PathVariable int a, @PathVariable int b, HttpServletRequest request) {

		logger.debug("starts");

		logger.info("Example CalculatorProcessor (/subtract) ->  a :{}, b:{}", a, b);

		logger.debug("ends");

		return new ResponseEntity<Integer>(service.multiply(a, b), HttpStatus.OK);
	}

	@RequestMapping(value = "/calculator/subtract/{a}/{b}", method = {
			RequestMethod.GET }, produces = "application/hal+json")
	public ResponseEntity<Integer> subtract(@PathVariable int a, @PathVariable int b, HttpServletRequest request) {

		logger.debug("starts");

		logger.info("Example CalculatorProcessor (/subtract) ->  a :{}, b:{}", a, b);

		logger.debug("ends");

		return new ResponseEntity<Integer>(service.subtract(a, b), HttpStatus.OK);
	}

	@RequestMapping(value = "/calculator/divide/{a}/{b}", method = {
			RequestMethod.GET }, produces = "application/hal+json")
	public ResponseEntity<Integer> divide(@PathVariable int a, @PathVariable int b, HttpServletRequest request) {

		logger.debug("starts");

		logger.info("Example CalculatorProcessor (/divide) ->  a :{}, b:{}", a, b);

		logger.debug("ends");

		return new ResponseEntity<Integer>(service.divide(a, b), HttpStatus.OK);
	}

}
