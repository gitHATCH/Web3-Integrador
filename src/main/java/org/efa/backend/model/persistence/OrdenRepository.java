package org.efa.backend.model.persistence;

import org.efa.backend.model.Orden;
import org.efa.backend.model.views.IConciliacionSlimView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrdenRepository extends JpaRepository<Orden, Long> {
    Optional<Orden> findByNumero(long numero);

    Optional<Orden> findById(Long id);

    void deleteById(Long id);

    /*@Query(value = "select a.*, b.masa as ProductoCargado, (NetoBalanza - b.masa) Diferencia \n" +
            "from (select numero, pesaje_inicial as PesajeInicial, pesaje_final as PesajeFinal, \n" +
            "(pesaje_final - pesaje_inicial) as NetoBalanza,\n" +
            "avg(densidad) as PromedioDensidad, avg(temperatura) as PromedioTemperatura, avg(caudal) as PromedioCaudal\n" +
            "from iw3final_db.ordenes o\n" +
            "left join iw3final_db.detalles_ordenes dor on o.id_detalle_orden = dor.id \n" +
            "left join iw3final_db.detalles_cargas dcr on dor.id = dcr.id_detalle_orden\n" +
            "where o.numero = :numero\n" +
            "group by 1,2,3,4) a\n" +
            "left join (select dcr.masa as masa, o.numero as num from iw3final_db.ordenes o\n" +
            "left join iw3final_db.detalles_ordenes dor on o.id_detalle_orden = dor.id \n" +
            "left join iw3final_db.detalles_cargas dcr on dor.id = dcr.id_detalle_orden\n" +
            "where o.numero = :numero\n" +
            "order by dcr.id desc\n" +
            "limit 1) b on a.numero = b.num", nativeQuery = true)
    IConciliacionSlimView getConciliacion(long numero);*/

    @Query(value = "select a.*, (a.PesajeFinal-a.PesajeInicial) as ProductoCargado, (NetoBalanza - (a.PesajeFinal-a.PesajeInicial)) Diferencia\n" +
            "            from (select numero, pesaje_inicial as PesajeInicial, pesaje_final as PesajeFinal, \n" +
            "            (pesaje_final - pesaje_inicial) as NetoBalanza,\n" +
            "            avg(densidad) as PromedioDensidad, avg(temperatura) as PromedioTemperatura, avg(caudal) as PromedioCaudal\n" +
            "            from iw3final_db.ordenes o\n" +
            "            left join iw3final_db.detalles_ordenes dor on o.id_detalle_orden = dor.id\n" +
            "            left join iw3final_db.detalles_cargas dcr on dor.id = dcr.id_detalle_orden\n" +
            "            where o.numero = :numero\n" +
            "            group by 1,2,3,4) a", nativeQuery = true)
    IConciliacionSlimView getConciliacion(long numero);
}
