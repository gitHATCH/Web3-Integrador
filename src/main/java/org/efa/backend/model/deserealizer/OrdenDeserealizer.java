package org.efa.backend.model.deserealizer;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.efa.backend.model.*;
import org.efa.backend.utils.JsonUtiles;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrdenDeserealizer extends StdDeserializer<Orden> {

    public OrdenDeserealizer(Class<?> src) {
        super(src);
    }


    @Override
    public Orden deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
        JsonNode node = jsonParser.readValueAsTree();
        JsonNode camionNode = node.get("camion");
        JsonNode cisternaNode = camionNode.get("datosCisterna");
        JsonNode choferNode = node.get("chofer");
        JsonNode clienteNode = node.get("cliente");
        JsonNode productoNode = node.get("producto");

        String numeroOrden = JsonUtiles.getString(node, "numeroOrden,numero_orden,numero-orden".split(","), null);
        String patente = JsonUtiles.getString(camionNode, "patente".split(","), null);
        String descripcion = JsonUtiles.getString(camionNode, "descripcion".split(","), null);
        String codigoExterno = JsonUtiles.getString(node,
                "codigoExterno,codigo,codigoExternoOrden,codigo-externo-orden,codigo-externo,ordenCodigoExterno,orden-codigo-externo".split(","),
                System.currentTimeMillis() + "");

        List<Cisternado> cisternadoList = new ArrayList<>();
        for (JsonNode nodo : cisternaNode) {
            Cisternado aux = new Cisternado();
            String tamanio = JsonUtiles.getString(nodo, "tamanio".split(","), null);
            aux.setTamanio(Long.parseLong(tamanio));
            cisternadoList.add(aux);
        }

        String totalCisterna = JsonUtiles.getString(camionNode, "totalCisterna,total_cisterna,total-cisterna".split(","), null);
        String dni = JsonUtiles.getString(choferNode, "dni".split(","), null);
        String nombre = JsonUtiles.getString(choferNode, "nombre".split(","), null);
        String apellido = JsonUtiles.getString(choferNode, "apellido".split(","), null);
        String razonSocial = JsonUtiles.getString(clienteNode, "razonSocial,razon_social,razon-social".split(","), null);
        String contacto = JsonUtiles.getString(clienteNode, "contacto".split(","), null);
        String id = JsonUtiles.getString(productoNode, "id".split(","), null);
        String nombreProducto = JsonUtiles.getString(productoNode, "nombre".split(","), null);
        String descripcionProducto = JsonUtiles.getString(productoNode, "descripcion".split(","), null);
        String preset = JsonUtiles.getString(node, "preset".split(","), null);
        String fechaTurnoCarga = JsonUtiles.getString(node, "fechaTurnoCarga,fecha_turno_carga,fecha-turno-carga".split(","), null);

        Orden r = new Orden();
        r.setNumeroOrden(Long.parseLong(numeroOrden));
        r.setCamion(Camion.builder().patente(patente).descripcion(descripcion).datosCisterna(cisternadoList).totalCisterna(Long.parseLong(totalCisterna)).build());
        r.setChofer(Chofer.builder().dni(Long.parseLong(dni)).nombre(nombre).apellido(apellido).build());
        r.setCliente(Cliente.builder().razonSocial(razonSocial).contacto(Long.parseLong(contacto)).build());
        r.setProducto(Producto.builder().id(Long.parseLong(id)).nombre(nombreProducto).descripcion(descripcionProducto).build());
        r.setPreset(Float.parseFloat(preset));
        r.setFechaTurnoCarga(OffsetDateTime.parse(fechaTurnoCarga));
        r.setCodigoExterno(codigoExterno);
        return r;
    }
}
