package name.nepavel.foodorder.restaservice.mapper;

import name.nepavel.foodorder.restaapi.model.FoodItem;
import name.nepavel.foodorder.restaservice.repository.FoodEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface FoodMapper {
    FoodMapper MAPPER = Mappers.getMapper(FoodMapper.class);

    @Mapping(source = "foodId", target = "id")
    FoodItem map(FoodEntity it);
}
