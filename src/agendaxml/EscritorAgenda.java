/*
 * Copyright (C) 2015 Dhaby Xiloj <dhabyx@gmail.com>
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package agendaxml;

import java.io.File;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

/**
 *
 * @author Dhaby Xiloj <dhabyx@gmail.com>
 */
public class EscritorAgenda {
    
    String nombreArchivo;
    
    ArrayList<Persona>personas;
    
    public EscritorAgenda(){
        nombreArchivo = new String();
        personas=new ArrayList<>();
    }
    
    public void guardar(String nombreArchivo) 
            throws ParserConfigurationException, TransformerException{
        Document documento = crearDocumento();
        crearEstructuraXML(documento);
        crearArchivo(documento, nombreArchivo);
    }
    
    private void crearArchivo(Document documento, String nombreArchivo) 
            throws TransformerConfigurationException, TransformerException {
        TransformerFactory fabricaTransformador = TransformerFactory.newInstance();
        Transformer transformador = fabricaTransformador.newTransformer();
        
        /* Opciones adicionales para el documento XML */
        /*   Agregando DTD */
        transformador.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, "agenda.dtd");
        /*   Permitiendo indentacion */
        transformador.setOutputProperty(OutputKeys.INDENT, "yes");
        /*   Espacios para la indentación */
        transformador.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
        /*  Omitir la cabecera XML */
        //transformador.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        /*  Omitir la cabecera standalone*/
        //documento.setXmlStandalone(true);
        
        DOMSource documentoFuente = new DOMSource(documento);
        
        StreamResult destino = new StreamResult(new File(nombreArchivo));
        
        /*
        * para salida a consola:
        StreamResult destino = new StreamResult(System.out);
        */
        
        transformador.transform(documentoFuente, destino);
        
    }
    
    private Document crearDocumento() throws ParserConfigurationException{
        DocumentBuilderFactory fabricaXML = DocumentBuilderFactory.newInstance();
        DocumentBuilder constructorXML = fabricaXML.newDocumentBuilder();
        Document documento = constructorXML.newDocument();
        return documento;
    }
    
    private void crearEstructuraXML(Document documento) {
        Element raiz = documento.createElement("agenda");
        documento.appendChild(raiz);
        
        for (Persona persona : personas) {
            Element nodoPersona = documento.createElement("persona");
            nodoPersona.setAttribute("codigo", persona.getCodigo());
            
            Element nodoNombres = documento.createElement("nombres");
            Element nodoApellidos = documento.createElement("apellidos");
            Element nodoTelefono = documento.createElement("telefono");
            Element nodoDireccion = documento.createElement("direccion");
            
            Text contenidoNombres = documento.createTextNode(persona.getNombres());
            Text contenidoApellidos = documento.createTextNode(persona.getApellidos());
            Text contenidoTelefono = documento.createTextNode(persona.getTelefono());
            Text contenidoDireccion = documento.createTextNode(persona.getDireccion());
            
            nodoNombres.appendChild(contenidoNombres);
            nodoApellidos.appendChild(contenidoApellidos);
            nodoTelefono.appendChild(contenidoTelefono);
            nodoDireccion.appendChild(contenidoDireccion);
            
            nodoPersona.appendChild(nodoNombres);
            nodoPersona.appendChild(nodoApellidos);
            nodoPersona.appendChild(nodoTelefono);
            nodoPersona.appendChild(nodoDireccion);
            
            raiz.appendChild(nodoPersona);
        }
    }

    public String getNombreArchivo() {
        return nombreArchivo;
    }

    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }

    public ArrayList<Persona> getPersonas() {
        return personas;
    }

    public void setPersonas(ArrayList<Persona> personas) {
        this.personas = personas;
    }
    
    public boolean addPersona(Persona persona){
        return this.personas.add(persona);
    }
    
}
