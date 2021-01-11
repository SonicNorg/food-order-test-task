package name.nepavel.foodorder.restaservice.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.*;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.sql.*;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

@Repository
@Transactional
@RequiredArgsConstructor
@Slf4j
public class OrderRepository {
    private static final AssortmentRepository.FoodEntityMapper itemMapper = new AssortmentRepository.FoodEntityMapper();
    private static final OrderEntityMapper orderMapper = new OrderEntityMapper();
    private final JdbcTemplate jdbcTemplate;

    @Value("${spring.flyway.placeholders.restaName}")
    private String tableName;
    @Value("${restaurant.db.schema}")
    private String schemaName;
    private String byIdQuery;
    private String allQuery;
    private String insertOrder;
    private String insertRows;

    @PostConstruct
    public void init() {
        byIdQuery = String.format("SELECT %1$s.%2$s_XREF.order_id, %1$s.%2$s_XREF.food_id, created, name, description, price, count FROM %1$s.%2$s_XREF LEFT JOIN %1$s.%2$s_ORDERS USING (order_id) LEFT JOIN %1$s.%2$s_ASSORTMENT USING (food_id) WHERE order_id = ? ORDER BY order_id", schemaName, tableName);
        allQuery = String.format("SELECT %1$s.%2$s_XREF.order_id, %1$s.%2$s_XREF.food_id, created, name, description, price, count FROM %1$s.%2$s_XREF LEFT JOIN %1$s.%2$s_ORDERS USING (order_id) LEFT JOIN %1$s.%2$s_ASSORTMENT USING (food_id) ORDER BY order_id", schemaName, tableName);
        insertOrder = String.format("INSERT INTO %s.%s_ORDERS (created) VALUES (?)", schemaName, tableName);
        insertRows = String.format("INSERT INTO %s.%s_XREF (order_id, food_id, count) VALUES (?, ?, ?)", schemaName, tableName);
    }

    public OrderEntity getById(int id) {
        List<OrderEntity> entities = jdbcTemplate.query(byIdQuery, new OrderExtractor(), id);
        return entities == null || entities.isEmpty() ? null : entities.get(0);
    }

    public Integer save(OrderEntity entity) {
        try(Connection connection = jdbcTemplate.getDataSource().getConnection()) {
            PreparedStatement statement = connection.prepareStatement(insertOrder, Statement.RETURN_GENERATED_KEYS);
            statement.setTimestamp(1, Timestamp.from(entity.getCreatedAt().toInstant()));
            int rowsInserted = statement.executeUpdate();
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (rowsInserted == 1 && generatedKeys.next()) {
                int id = generatedKeys.getInt(1);
                entity.getFoods().forEach((key, value) -> jdbcTemplate.update(insertRows, id, key.getFoodId(), value));
                return id;
            }
            throw new RuntimeException("Failed to insert order!");
        } catch (SQLException throwables) {
            log.error("ERROR!", throwables);
            throw new RuntimeException("Failed to insert order!", throwables);
        }
    }

    public List<OrderEntity> list() {
        List<OrderEntity> entities = jdbcTemplate.query(allQuery, new OrderExtractor());
        return entities == null || entities.isEmpty() ? Collections.emptyList() : entities;
    }

    private static class OrderEntityMapper implements RowMapper<OrderEntity> {
        @Override
        public OrderEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new OrderEntity().setOrderId(rs.getInt("order_id"))
                    .setCreatedAt(rs.getTimestamp("created").toLocalDateTime().atOffset(ZoneOffset.UTC));
        }
    }

    private static class OrderExtractor implements ResultSetExtractor<List<OrderEntity>> {
        @Override
        public List<OrderEntity> extractData(ResultSet rs) throws SQLException, DataAccessException {
            List<OrderEntity> results = new ArrayList<>();

            OrderEntity current = null;
            int rowNum = 0;
            int currOrder = -1;
            while (rs.next()) {
                int orderId = rs.getInt("order_id");
                if (orderId != currOrder) {
                    current = orderMapper.mapRow(rs, rowNum);
                    currOrder = orderId;
                    results.add(current);
                }
                current.getFoods().put(itemMapper.mapRow(rs, rowNum++), rs.getInt("count"));
            }
            return results;
        }
    }
}
