package pl.zajavka.business;

import pl.zajavka.domain.Opinion;

import java.util.List;

public interface OpinionRepository {
    Opinion createOpinion(Opinion opinion);

    List<Opinion> findAll();

    List<Opinion> findAll(String email);

    List<Opinion> findAllByProduct(String productCode);

    List<Opinion> findUnwantedOpinions();
    // https://www.postgresqltutorial.com/postgresql-tutorial/postgresql-delete-join/
    // W przypadku PostgreSql to nie zadziała:

    // DELETE FROM OPINION AS OPN INNER JOIN CUSTOMER AS CUS ON CUS.ID = OPN.CUSTOMER_ID WHERE CUS.EMAIL = :email

    void removeAll(String email);

    void removeAllByProduct(String productCode);

    void removeAll();

    void removeUnwantedOpinions();

    boolean customerGivesUnwantedOpinions(String email);
}
