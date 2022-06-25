package com.dashboard.doctor_dashboard.services;

import com.dashboard.doctor_dashboard.entities.Prescription;
import com.dashboard.doctor_dashboard.entities.dtos.PatientDto;
import com.dashboard.doctor_dashboard.exceptions.ReportNotFound;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.FontSelector;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;
import java.util.List;

@Service
public class PdFGeneratorServiceImpl {
    public void generatePdf(List<Prescription> prescriptions, PatientDto patientDto,String notes1) throws  IOException
    {
        OutputStream file=new FileOutputStream("/home/nineleaps/Downloads/prescription/prescription.pdf");
        try
        {
            System.out.println("in");

            Document document = new Document(PageSize.A4, 20, 20, 20, 20);
            FontSelector fs = new FontSelector();
            FontSelector fs1 = new FontSelector();
            Font font = FontFactory.getFont("Roboto", 50, Font.BOLD);

            Font font1 = FontFactory.getFont("Roboto", 16, Font.NORMAL);

            font.setColor(90,84,150);
            fs.addFont(font);
            fs1.addFont(font1);
            PdfWriter.getInstance(document, file);


            Phrase date1 = fs1.process("Date: "+ formatDate(LocalDate.now().toString()));
            Paragraph date = new Paragraph(date1);
            date.setAlignment(Element.ALIGN_RIGHT);




            Phrase name=fs.process("meCare");
            Paragraph name1=new Paragraph(name);
            name1.setAlignment(Element.ALIGN_LEFT);

            Paragraph name2=new Paragraph("           ");


          Paragraph intro=new Paragraph(setintro("Prescription Details"));
          intro.setAlignment(Element.ALIGN_CENTER);

          Paragraph intro2=new Paragraph(setintro("Medication:"));
          intro2.setAlignment(Element.ALIGN_LEFT);

          Paragraph intro3=new Paragraph(setintro("Notes:"));
          intro3.setAlignment(Element.ALIGN_LEFT);


           PdfPTable details = new PdfPTable(5);
           details.setWidthPercentage(100);
           details.addCell(getBillHeaderCell("Patient Name"));
           details.addCell(getBillHeaderCell("Age"));
           details.addCell(getBillHeaderCell("Gender"));
           details.addCell(getBillHeaderCell("Doctor Name"));
           details.addCell(getBillHeaderCell("Speciality"));




            PdfPTable notes = new PdfPTable(1);
           notes.setWidthPercentage(100);
           notes.addCell(new Paragraph(notes1+"                      \n"+"\n"+"\n"+"\n"+"\n"+"\n"));
            System.out.println("nothing:1");

            System.out.println(patientDto.toString());
            details.addCell(getBillRowCell(patientDto.getPatientName()));
            details.addCell(getBillRowCell(String.valueOf(patientDto.getAge())));
            details.addCell(getBillRowCell(patientDto.getGender()));
            details.addCell(getBillRowCell(patientDto.getDoctorName()));
            details.addCell(getBillRowCell(patientDto.getCategory()));

            System.out.println("nothing:2");



            PdfPTable billTable = new PdfPTable(5);
            billTable.setWidthPercentage(100);
            billTable.addCell(getBillHeaderCell("Drug Name"));
            billTable.addCell(getBillHeaderCell("Quantity"));
            billTable.addCell(getBillHeaderCell("Type"));
            billTable.addCell(getBillHeaderCell("Days"));
            billTable.addCell(getBillHeaderCell("Time"));


            for(int i=0;i<prescriptions.size();i++) {
                billTable.addCell(getBillRowCell(prescriptions.get(i).getDrugName()));
                billTable.addCell(getBillRowCell(prescriptions.get(i).getQuantity().toString()));
                billTable.addCell(getBillRowCell(prescriptions.get(i).getType()));
                billTable.addCell(getBillRowCell(prescriptions.get(i).getDays().toString()));
                billTable.addCell(getBillRowCell(prescriptions.get(i).getTime()));
            }
            System.out.println("nothing3");


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

        }
        catch (Exception e) {
            System.out.println(e);
            file.close();
            throw new ReportNotFound(e.getMessage());
        }
    }
    public static PdfPCell getBillHeaderCell(String text) {
        FontSelector fs = new FontSelector();
        Font font = FontFactory.getFont(FontFactory.HELVETICA, 15, Font.BOLD);
        font.setColor(BaseColor.BLUE);
        fs.addFont(font);
        Phrase phrase = fs.process(text);
        PdfPCell cell = new PdfPCell (phrase);
        cell.setHorizontalAlignment (Element.ALIGN_CENTER);
        cell.setPadding (5.0f);
        return cell;
    }
    public static PdfPCell getBillRowCell(String text) {
        FontSelector fs = new FontSelector();
        Font font = FontFactory.getFont(FontFactory.HELVETICA, 15,Font.NORMAL);
        font.setColor(90,84,150);

        fs.addFont(font);
        Phrase phrase=fs.process(text);

        PdfPCell cell = new PdfPCell (phrase);
        cell.setHorizontalAlignment (Element.ALIGN_CENTER);
        cell.setPadding (5.0f);
        cell.setBorderWidthBottom(1);
        cell.setVerticalAlignment(1);
        cell.setBorderWidthTop(0);
        return cell;
    }
    public static PdfPCell getBillRowCellValue(String text) {
        FontSelector fs = new FontSelector();
        Font font = FontFactory.getFont(FontFactory.HELVETICA, 15);
        font.setColor(90,84,150);
        fs.addFont(font);
        Phrase phrase=fs.process(text);

        PdfPCell cell = new PdfPCell (phrase);
        cell.setHorizontalAlignment (Element.ALIGN_CENTER);
        cell.setPadding (5.0f);
        cell.setBorderWidthBottom(1);
        cell.setBorderWidthTop(0);
        return cell;
    }
    public static Phrase setintro(String text)
    {
        FontSelector fs = new FontSelector();
        Font font = FontFactory.getFont(FontFactory.TIMES_ITALIC, 20,Font.UNDERLINE);
        font.setColor(1,1,1);
        fs.addFont(font);
        return fs.process(text);
    }

    String formatDate(String Date1){
        String[] newArray = Date1.split("-",5);
        String newDate = newArray[2]+"-"+newArray[1]+"-"+newArray[0];
        return newDate;
    }
}
