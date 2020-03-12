package com.buyify.rest;

import com.buyify.rest.entities.Order;
import com.buyify.rest.entities.Product;
import com.buyify.rest.entities.User;
import com.buyify.rest.repositories.OrderRepository;
import com.buyify.rest.repositories.UserRepository;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Optional;


@RestController
public class BuyifyRestApplicationController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    public JavaMailSender emailSender;

    @RequestMapping(value = "/generarFactura/{id}", method = RequestMethod.GET)
    public ResponseEntity<byte[]> generatePDF(@PathVariable long id) {
        Optional<Order> order = orderRepository.findById(id);

        String filename = "factura_" + id + ".pdf";

        ByteArrayOutputStream output;
        try {
            output = createInvoice(order.get());
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.set("Content-Disposition", "attachment; filename=\"" + filename + "\"");

        return new ResponseEntity<>(output.toByteArray(), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/mail/{id}", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public void email(@PathVariable long id) {
        Optional<User> user = userRepository.findById(id);

        System.out.println("Enviando correo a: " + user.get().getEmail());

        sendMail(user.get().getEmail(), "Bienvenido a Buyify",
                "Bienvenido a Buyify\nTu usuario es: "+user.get().getUsername());
    }
    
    @RequestMapping(value = "/realizado/{id_order}", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public void OrderEmail(@PathVariable long id_order) {
        Optional<Order> order = orderRepository.findById(id_order);
        String email = order.get().getUser().getEmail();
        StringBuilder order_concat = new StringBuilder();
        for(Product p : order.get().getProducts()) {
            order_concat.append(p.getName()+","+p.getPrice()+","+p.getDescription()+"\n");
        }
        System.out.println("Enviando correo a: " + email);

        sendMail(email, "Pedido Realizado",
                "Su pedido ha sido procesado correctamente\n"
                +order_concat);
    }

    private ByteArrayOutputStream createInvoice(Order order) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

        PDDocument document = new PDDocument();
        PDPage page = new PDPage(PDRectangle.A4);
        document.addPage(page);

        PDPageContentStream contentStream = new PDPageContentStream(document, page);

        contentStream.beginText();
        contentStream.setFont(PDType1Font.TIMES_BOLD, 26);
        contentStream.setLeading(40f);
        contentStream.newLineAtOffset(60, 760);
        contentStream.showText("Pedido nº " + order.getId());
        contentStream.newLine();

        contentStream.setFont(PDType1Font.TIMES_ROMAN, 14);
        contentStream.setLeading(16f);

        contentStream.showText(order.getUser().getName());
        contentStream.newLine();
        contentStream.showText("Fecha: " + formatter.format(order.getDate()));
        contentStream.newLine();
        contentStream.newLine();

        for (Product product : order.getProducts()) {
            contentStream.showText(product.getName() + " - " + product.getPrice() + "€");
            contentStream.newLine();
        }
        contentStream.endText();
        contentStream.close();

        document.save(output);

        return output;
    }

    public void sendMail(String address, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(address);
        message.setSubject(subject);
        message.setText(text);

        emailSender.send(message);
    }

}
