package ro.iopo.gnir.controller;

import net.sourceforge.tess4j.TesseractException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ro.iopo.gnir.service.InvoiceConverter;
import ro.iopo.gnir.service.NotificationService;

import java.io.IOException;

@RestController
public class GnirController implements GnirControllerAPI {

    private final InvoiceConverter invoiceConverter;
    private final NotificationService notificationService;

    @Autowired
    public GnirController(InvoiceConverter invoiceConverter, NotificationService notificationService) {
        this.invoiceConverter = invoiceConverter;
        this.notificationService = notificationService;
    }

    @Override
    public ResponseEntity<String> testFunctionality() throws IOException {
        String result = this.notificationService.notifyIfOffline("MY_ID");
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> parseImage() {
        return new ResponseEntity<>(invoiceConverter.convertScannedPdf(), HttpStatus.OK);
    }
}
