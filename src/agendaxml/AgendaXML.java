/*
 * Copyright (C) 2015 Dhaby Xiloj <dhabyx@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package agendaxml;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.w3c.dom.Document;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 *
 * @author Dhaby Xiloj <dhabyx@gmail.com>
 */
public class AgendaXML {
    
    public Document cargarXML(String archivo) 
            throws ParserConfigurationException, IOException, SAXException {
        
        DocumentBuilderFactory fabricaXML = DocumentBuilderFactory.newInstance();
        fabricaXML.setValidating(true);
        
        DocumentBuilder constructorXML = fabricaXML.newDocumentBuilder();
                
        ErrorHandler capturadorErrores = new CapturadorError();
        constructorXML.setErrorHandler(capturadorErrores);
        
        Document documentoXML;
        documentoXML = constructorXML.parse(archivo);
        documentoXML.getDocumentElement().normalize();
        return documentoXML;
    }
    
    public void leerXML(String archivoXML) {
        
        try {            
            
            Document doc = cargarXML(archivoXML);
            LectorAgenda lectorAgenda = new LectorAgenda(doc);
            
            System.out.println("Elemento raiz: "+lectorAgenda.nombreNodoRaiz());
            System.out.println();
            
            System.out.println("Imprimiendo nodos hijos del nodo Raiz");
            lectorAgenda.imprimirNodosHijos(lectorAgenda.nodoRaiz());
            System.out.println();
            
            System.out.println("Imprimiendo el primer contacto de la agenda");
            Persona primerContacto = lectorAgenda.obtenerPrimerContacto();
            System.out.println(primerContacto);
            
            
        } catch (ParserConfigurationException ex) {
            System.err.println("Error al tratar de crear un objeto lector de XML");
            Logger.getLogger(AgendaXML.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            System.err.println("Error al tratar de leer el archivo "+archivoXML);
            Logger.getLogger(AgendaXML.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            Logger.getLogger(AgendaXML.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void crearXML(String archivoXML){
        
        Persona persona1 = new Persona("p1","Fulano","Perez","123423","En algún lugar");
        Persona persona2 = new Persona("p2","Mengano","Perez","987351","En otro lugar");
        
        EscritorAgenda escritor = new EscritorAgenda();
        escritor.addPersona(persona1);
        escritor.addPersona(persona2);
        
        try {
            escritor.guardar(archivoXML);
        } catch (ParserConfigurationException ex) {
            System.err.println("Error en la estructura del documento XML");
            Logger.getLogger(AgendaXML.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerException ex) {
            System.err.println("Error al momento de intentar almacenar la información");
            Logger.getLogger(AgendaXML.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String archivoXML = "src"+File.separator
                +"agendaxml"+File.separator
                +"agenda.xml";
        
        String archivoXMLSalida = "src"+File.separator
                +"agendaxml"+File.separator
                +"agenda2.xml";
        
        AgendaXML agenda = new AgendaXML();
        
        agenda.leerXML(archivoXML);
        agenda.crearXML(archivoXMLSalida);
    }
    
    private static class CapturadorError implements ErrorHandler {

        @Override
        public void warning(SAXParseException exception) throws SAXException {
            System.out.println("Peligro: ");
            printInfo(exception);
            //throw exception;
        }

        @Override
        public void error(SAXParseException exception) throws SAXException {
            System.err.println("Error: ");
            printInfo(exception);
            throw exception;
        }

        @Override
        public void fatalError(SAXParseException exception) throws SAXException {
            System.err.println("Error Fatal: ");
            printInfo(exception);
            throw exception;
        }
        
        private void printInfo(SAXParseException e) {
         System.err.println("   Public ID: "+e.getPublicId());
         System.err.println("   System ID: "+e.getSystemId());
         System.err.println("   Numero de línea: "+e.getLineNumber());
         System.err.println("   Número de columna: "+e.getColumnNumber());
         System.err.println("   Mensaje: "+e.getMessage());
      }
        
    }
    
}
