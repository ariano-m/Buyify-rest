package com.buyify.rest;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.buyify.rest.entities.Order;
import com.buyify.rest.entities.Product;
import com.buyify.rest.entities.User;
import com.buyify.rest.repositories.OrderRepository;
import com.buyify.rest.repositories.UserRepository;



@RestController
public class BuyifyRestApplicationController {
	
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private UserRepository userRepository;
	@RequestMapping(value = "/generarFactura/{username}", method = RequestMethod.GET, produces = "application/octet-stream")
	public ResponseEntity<byte[]> generatePDF(@PathVariable String username) {
		System.out.println("Servicio interno");
		User user = userRepository.findByUsername(username);
		List<Order> orders = orderRepository.findByUser(user);
		
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(user.getUsername()).append("\n");
		
		for(Order pedido : orders) {
			stringBuilder.append(pedido.getId()).append(" " + pedido.getDate()).append("\n");
			List<Product> products = pedido.getProducts();
			
			for(Product product : products) {
				stringBuilder.append(product.getName() + " " + product.getPrice() + " " + product.getPromotion().getPromotion() + "\n");
			}
			
			stringBuilder.append("\t");
		}
		
		
		try {
			outputStream.write(String.valueOf(stringBuilder).getBytes());
//			byte[] pdf = outputStream.toByteArray();
			byte[] pdf = "esto es una prueba".getBytes();
			return new ResponseEntity<byte[]>(pdf, HttpStatus.CREATED);
			
			
			
			
		} catch (IOException e) {
			e.printStackTrace();
			
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
}
