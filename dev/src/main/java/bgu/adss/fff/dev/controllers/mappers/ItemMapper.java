package bgu.adss.fff.dev.controllers.mappers;

import bgu.adss.fff.dev.contracts.ItemDto;
import bgu.adss.fff.dev.domain.models.Item;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class ItemMapper {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd LLLL yyyy");

    public static ItemDto map(Item item){
        return new ItemDto(
                item.getItemID(),
                item.getExpirationDate().format(formatter),
                item.isDefected()
        );
    }

    public static List<ItemDto> map(List<Item> items){
        return items.stream().map(ItemMapper::map).collect(Collectors.toList());
    }

}
