package com.github.rafaellbarros.ifood.cadastro.panache;

import com.github.rafaellbarros.ifood.cadastro.model.entity.Restaurante;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Parameters;
import io.quarkus.panache.common.Sort;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.HashMap;
import java.util.Map;

public class PanacheQueries {

    public void exemplosSelects() {
        // -- Classe --

        //findAll

        PanacheQuery<Restaurante> findAll = Restaurante.findAll();
        Restaurante.findAll(Sort.by("nome").and("id", Sort.Direction.Ascending));

        PanacheQuery<Restaurante> page = findAll.page(Page.of(3, 10));

        //find sem sort

        Map<String, Object> params = new HashMap<>();
        params.put("nome", "");
        Restaurante.find("select r from Restaurante where nome = :nome", params);

        String nome = "";
        Restaurante.find("select r from Restaurante where nome = $1", nome);

        Restaurante.find("select r from Restaurante where nome = :nome and id = :id",
                Parameters.with("nome", "").and("id", 1L));

        //Atalho para findAll.stream, mas precisa de transacao se nao o cursor pode fechar antes

        Restaurante.stream("select r from Restaurante where nome = :nome", params);

        Restaurante.stream("select r from Restaurante where nome = $1", nome);

        Restaurante.stream("select r from Restaurante where nome = :nome and id = :id",
                Parameters.with("nome", "").and("id", 1L));

        //find by id

        Restaurante findById = Restaurante.findById(1L);

        //Persist

        Restaurante.persist(findById, findById);

        //Delete

        Restaurante.delete("delete Restaurante where nome = :nome", params);

        Restaurante.delete("delete Restaurante where nome = $1", nome);

        Restaurante.delete("nome = :nome and id = :id",
                Parameters.with("nome", "").and("id", 1L));

        //Update

        Restaurante.update("update Restaurante where nome = :nome", params);

        Restaurante.update("update Restaurante where nome = $1", nome);

        //Count

        Restaurante.count();

        //-- Métodos de objeto--

        Restaurante restaurante = new Restaurante();

        restaurante.persist();

        restaurante.isPersistent();

        restaurante.delete();

    }

}

@Entity
class MinhaEntidade1 extends PanacheEntity {
    public String nome;
}

@Entity
class MinhaEntidade2 extends PanacheEntityBase {

    @Id
    @GeneratedValue
    public Long id;

    public String nome;

    public MinhaEntidade2() {
    }
}

//@Entity
class MinhaEntidade3 {

    @Id
    @GeneratedValue
    public Long id;

    public String nome;

    public MinhaEntidade3() {
    }
}

//@ApplicationScoped
class MeuRepositorio implements PanacheRepository<MinhaEntidade3> {

}

