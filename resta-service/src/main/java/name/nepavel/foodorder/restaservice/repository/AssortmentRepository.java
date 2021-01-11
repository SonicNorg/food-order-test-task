package name.nepavel.foodorder.restaservice.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class AssortmentRepository {
    private final JdbcTemplate jdbcTemplate;

    @Value("${spring.flyway.placeholders.restaName}")
    private String tableName;
    @Value("${restaurant.db.schema}")
    private String schemaName;
    private String query;
    private final RowMapper<FoodEntity> mapper = new FoodEntityMapper();

    @PostConstruct
    public void init() {
        query = String.format("SELECT * FROM %s.%s_ASSORTMENT", schemaName, tableName);
    }

    public List<FoodEntity> findAll() {
        return jdbcTemplate.queryForStream(query, mapper).collect(Collectors.toList());
    }

    public static class FoodEntityMapper implements RowMapper<FoodEntity> {
        @Override
        public FoodEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new FoodEntity(rs.getInt("food_id"), rs.getString("name"), rs.getString("description"), rs.getFloat("price"));
        }
    }
}
