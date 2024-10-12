package ro.iopo.gnir.service;

import com.asprise.ocr.Ocr;
import lombok.extern.slf4j.Slf4j;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

@Slf4j
@Service
public class InvoiceConverter {


    public String convertScannedPdf() {
        Ocr.setUp(); // one time setup
        Ocr ocr = new Ocr(); // create a new OCR engine
        ocr.startEngine("eng", Ocr.SPEED_SLOW); // English
        String s = ocr.recognize(new File[]{new File("app/src/main/resources/factura.pdf")},
            Ocr.RECOGNIZE_TYPE_ALL, Ocr.OUTPUT_FORMAT_PLAINTEXT);

        ocr.stopEngine();
        return s;
    }

    public String convertScannedPdfWithTesseract() throws IOException, TesseractException {
        return parseScannedPdf();
    }

    private String parseScannedPdf() throws TesseractException {

        // Path to the scanned file
        String filePath = "app/src/main/resources/factura4.pdf";

        // Extract images from file
        StringBuilder out = new StringBuilder();
        Tesseract tesseract = new Tesseract();
        tesseract.setLanguage("ron");
        tesseract.setDatapath("app/src/main/resources");
        String result = tesseract.doOCR(new File(filePath));
        out.append(result);

        return out.toString();
    }

    private String extractTextFromScannedDocument(PDDocument document) throws IOException, TesseractException {

        // Extract images from file
        PDFRenderer pdfRenderer = new PDFRenderer(document);
        StringBuilder out = new StringBuilder();

        Tesseract tesseract = new Tesseract();
        tesseract.setDatapath("app/src/main/resources");
        tesseract.setLanguage("ron");

        for (int page = 0; page < document.getNumberOfPages(); page++) {
            BufferedImage bufferedImage = pdfRenderer.renderImageWithDPI(page, 200, ImageType.RGB);

            // Create a temp image file
            File tempFile = File.createTempFile("tempfile_" + page, ".png");
            ImageIO.write(bufferedImage, "png", tempFile);

            String result = tesseract.doOCR(tempFile);
            out.append(result);

            // Delete temp file
            tempFile.delete();

        }

        return out.toString();

    }

}
