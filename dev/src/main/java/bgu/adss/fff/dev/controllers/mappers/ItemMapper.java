package bgu.adss.fff.dev.controllers.mappers;

import bgu.adss.fff.dev.contracts.ItemDto;
import bgu.adss.fff.dev.contracts.RequestItemDto;
import bgu.adss.fff.dev.domain.models.Item;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ItemMapper {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public static ItemDto map(Item item){
        return new ItemDto(
                item.getItemID(),
                item.getExpirationDate().format(formatter),
                item.isDefected()
        );
    }

    public static ItemDto[] map(List<Item> items){
        return items.stream().map(ItemMapper::map).toArray(ItemDto[]::new);
    }

    public static Item map(ItemDto itemDto){
        return new Item(
                itemDto.itemID(),
                LocalDate.parse(itemDto.expirationDate(), formatter),
                itemDto.isDefected()
        );
    }

    public static List<Item> map(ItemDto[] itemDTOs) {
        return Stream.of(itemDTOs).map(ItemMapper::map).collect(Collectors.toList());
    }

    public static List<Item> map(RequestItemDto items) {
        List<Item> itemList = new LinkedList<>();
        for (int i = 0; i < items.amount(); i++) {
            itemList.add(new Item(
                    0,
                    LocalDate.parse(items.expirationDate(), formatter),
                    items.isDefected()
            ));
        }

        return itemList;
    }

}
