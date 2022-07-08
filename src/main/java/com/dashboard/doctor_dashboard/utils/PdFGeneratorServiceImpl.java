package com.dashboard.doctor_dashboard.utils;

import com.dashboard.doctor_dashboard.entities.model.Prescription;
import com.dashboard.doctor_dashboard.entities.dtos.PatientDto;
import com.dashboard.doctor_dashboard.exceptions.ResourceNotFoundException;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.FontSelector;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
public class PdFGeneratorServiceImpl {
    public void generatePdf(List<Prescription> prescriptions, PatientDto patientDto,String notes1) throws  IOException
    {
        log.info(" PDF Service Started");

        OutputStream file=new FileOutputStream("/home/nineleaps/Downloads/prescription/prescription.pdf");
        try
        {

            var document = new Document(PageSize.A4, 20, 20, 20, 20);
            var fs = new FontSelector();
            var fs1 = new FontSelector();
            var font = FontFactory.getFont("Roboto", 50, Font.BOLD);

            var font1 = FontFactory.getFont("Roboto", 16, Font.NORMAL);

            font.setColor(90,84,150);
            fs.addFont(font);
            fs1.addFont(font1);
            PdfWriter.getInstance(document, file);


            Phrase date1 = fs1.process("Date: "+ formatDate(LocalDate.now().toString()));
            var date = new Paragraph(date1);
            date.setAlignment(Element.ALIGN_RIGHT);




            Phrase name=fs.process("meCare");
            var name1=new Paragraph(name);
            name1.setAlignment(Element.ALIGN_LEFT);

            var name2=new Paragraph("           ");


          var intro=new Paragraph(setintro("Prescription Details"));
          intro.setAlignment(Element.ALIGN_CENTER);

          var intro2=new Paragraph(setintro("Medication:"));
          intro2.setAlignment(Element.ALIGN_LEFT);

          var intro3=new Paragraph(setintro("Notes:"));
          intro3.setAlignment(Element.ALIGN_LEFT);


           var details = new PdfPTable(5);
           details.setWidthPercentage(100);
           details.addCell(getBillHeaderCell("Patient Name"));
           details.addCell(getBillHeaderCell("Age"));
           details.addCell(getBillHeaderCell("Gender"));
           details.addCell(getBillHeaderCell("Doctor Name"));
           details.addCell(getBillHeaderCell("Speciality"));




            var notes = new PdfPTable(1);
           notes.setWidthPercentage(100);
           notes.addCell(new Paragraph(notes1+"                      \n"+"\n"+"\n"+"\n"+"\n"+"\n"));

            details.addCell(getBillRowCell(patientDto.getPatientName()));
            details.addCell(getBillRowCell(String.valueOf(patientDto.getAge())));
            details.addCell(getBillRowCell(patientDto.getGender()));
            details.addCell(getBillRowCell(patientDto.getDoctorName()));
            details.addCell(getBillRowCell(patientDto.getCategory()));




            var billTable = new PdfPTable(5);
            billTable.setWidthPercentage(100);
            billTable.addCell(getBillHeaderCell("Drug Name"));
            billTable.addCell(getBillHeaderCell("Quantity"));
            billTable.addCell(getBillHeaderCell("Type"));
            billTable.addCell(getBillHeaderCell("Days"));
            billTable.addCell(getBillHeaderCell("Time"));


            for(var i=0;i<prescriptions.size();i++) {
                billTable.addCell(getBillRowCell(prescriptions.get(i).getDrugName()));
                billTable.addCell(getBillRowCell(prescriptions.get(i).getQuantity().toString()));
                billTable.addCell(getBillRowCell(prescriptions.get(i).getType()));
                billTable.addCell(getBillRowCell(prescriptions.get(i).getDays().toString()));
                billTable.addCell(getBillRowCell(prescriptions.get(i).getTime()));
            }


            document.open();
            document.add(name2);
            document.add(name1);
            document.add(date);
            document.add(name2);
            document.add(name2);
            document.add(name2);
            document.add(name2);
            document.add(name2);
            document.add(details);

            document.add(name2);
            document.add(name2);
            document.add(name2);
            document.add(name2);




            document.add(intro);
            document.add(name2);
            document.add(name2);

            document.add(intro2);
            document.add(name2);

            document.add(billTable);
            document.add(name2);

            document.add(intro3);
            document.add(name2);

            document.add(notes);


            document.setMarginMirroringTopBottom(true);

            document.close();
            file.close();
            log.info("PDF Service Stopped");
        }
        catch (Exception e) {
            file.close();
            log.info("PDF Service Stopped");
            throw new ResourceNotFoundException(e.getMessage());
        }
    }
    public static PdfPCell getBillHeaderCell(String text) {
        var fs = new FontSelector();
        var font = FontFactory.getFont(FontFactory.HELVETICA, 15, Font.BOLD);
        font.setColor(BaseColor.BLUE);
        fs.addFont(font);
        var phrase = fs.process(text);
        var cell = new PdfPCell (phrase);
        cell.setHorizontalAlignment (Element.ALIGN_CENTER);
        cell.setPadding (5.0f);
        return cell;
    }
    public static PdfPCell getBillRowCell(String text) {
        var fs = new FontSelector();
        var font = FontFactory.getFont(FontFactory.HELVETICA, 15,Font.NORMAL);
        font.setColor(90,84,150);

        fs.addFont(font);
        var phrase=fs.process(text);

        var cell = new PdfPCell (phrase);
        cell.setHorizontalAlignment (Element.ALIGN_CENTER);
        cell.setPadding (5.0f);
        cell.setBorderWidthBottom(1);
        cell.setVerticalAlignment(1);
        cell.setBorderWidthTop(0);
        return cell;
    }
    public static PdfPCell getBillRowCellValue(String text) {
        var fs = new FontSelector();
        var font = FontFactory.getFont(FontFactory.HELVETICA, 15);
        font.setColor(90,84,150);
        fs.addFont(font);
        var phrase=fs.process(text);

        var cell = new PdfPCell (phrase);
        cell.setHorizontalAlignment (Element.ALIGN_CENTER);
        cell.setPadding (5.0f);
        cell.setBorderWidthBottom(1);
        cell.setBorderWidthTop(0);
        return cell;
    }
    public static Phrase setintro(String text)
    {
        var fs = new FontSelector();
        var font = FontFactory.getFont(FontFactory.TIMES_ITALIC, 20,Font.UNDERLINE);
        font.setColor(1,1,1);
        fs.addFont(font);
        return fs.process(text);
    }

    public String formatDate(String date1){
        String[] newArray = date1.split("-",5);
        return newArray[2]+"-"+newArray[1]+"-"+newArray[0];
    }

    public String monthHandler(String month){
        if(Integer.parseInt(month)>10){
            return "0"+month;
        }
        return month;
    }

    public Boolean dateHandler(LocalDate date){
        return date.isAfter(LocalDate.now()) && date.isBefore(LocalDate.now().plusDays(8));
    }
}
