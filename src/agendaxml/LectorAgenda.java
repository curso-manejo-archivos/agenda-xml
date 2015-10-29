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

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author Dhaby Xiloj <dhabyx@gmail.com>
 */
public class LectorAgenda {
    
    Document documento;
    
    public LectorAgenda(Document documento){
        this.documento = documento;
    }
    
    public String nombreNodoRaiz(){
        return documento.getDocumentElement().getNodeName();
    }
    
    public Node nodoRaiz() {
        return documento.getDocumentElement();
    }
    
    public void imprimirNodosHijos(Node nodo) {
        NodeList nodosHijos = nodo.getChildNodes();
        for (int i = 0; i < nodosHijos.getLength(); i++) {
            System.out.println("Nodo "+i+": "+nodosHijos.item(i).getNodeName());
            if (nodosHijos.item(i).getNodeType() == Node.TEXT_NODE) {
                System.out.println("Contenido de nodo "+i+": \""
                        +nodosHijos.item(i).getNodeValue()+"\"");
            }
        }
    }
    
    public Persona obtenerPrimerContacto() {
        /*
        * agenda (DocumentElement)
        *  |--text (firstChild de DocumentElement)
        *  |--persona (sibling o hermano de text)
        */
        Node nodoTexto = documento.getDocumentElement().getFirstChild();
        Node nodoPersona = nodoTexto.getNextSibling();
                
        if ( nodoPersona.getNodeType() == Node.ELEMENT_NODE ) {
            Persona persona = new Persona();
            
            Element elementoPersona = (Element)nodoPersona;
            persona.setCodigo(elementoPersona.getAttribute("codigo"));
            
            NodeList hijosPersona = nodoPersona.getChildNodes();
            for ( int i=0; i < hijosPersona.getLength(); i++ ) {
                if ( hijosPersona.item(i).getNodeType() == Node.ELEMENT_NODE ) {
                    Element elemento = (Element)hijosPersona.item(i);
                    switch (elemento.getTagName()) {
                        case "nombres":
                            persona.setNombres(elemento.getTextContent());
                            break;
                        case "apellidos":
                            persona.setApellidos(elemento.getTextContent());
                            break;
                        case "telefono":
                            persona.setTelefono(elemento.getTextContent());
                            break;
                        case "direccion":
                            persona.setDireccion(elemento.getTextContent());
                            break;
                        default:
                            break;
                    }
                }
            }
            return persona;
        }
         return null;
    }
}
