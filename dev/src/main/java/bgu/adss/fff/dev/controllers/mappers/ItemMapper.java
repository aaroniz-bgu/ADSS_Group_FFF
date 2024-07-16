package bgu.adss.fff.dev.controllers.mappers;

import bgu.adss.fff.dev.contracts.ItemDto;
import bgu.adss.fff.dev.contracts.RequestItemDto;
import bgu.adss.fff.dev.domain.models.Branch;
import bgu.adss.fff.dev.domain.models.Item;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ItemMapper {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    /**
     * Maps an item to an itemDto
     * @param item item to be mapped to itemDto
     * @return itemDto
     */
    public static ItemDto map(Item item){
        return new ItemDto(
                item.getItemID(),
                item.getExpirationDate().format(formatter),
                item.isDefected(),
                item.getBranch().getName()
        );
    }

    /**
     * Maps an array of items to an array of itemDtos
     * @param items array of items to be mapped to array of itemDtos
     * @return array of itemDtos
     */
    public static ItemDto[] map(List<Item> items){
        return items.stream().map(ItemMapper::map).toArray(ItemDto[]::new);
    }

    /**
     * Maps an itemDto to an item
     * @param itemDto itemDto to be mapped to item
     * @return item
     */
    public static Item map(ItemDto itemDto){
        return new Item(
                itemDto.itemID(),
                LocalDate.parse(itemDto.expirationDate(), formatter),
                itemDto.isDefected(),
                new Branch(itemDto.branch())
        );
    }

    /**
     * Maps an array of itemDtos to an array of items
     * @param itemDTOs array of itemDtos to be mapped to array of items
     * @return array of items
     */
    public static List<Item> map(ItemDto[] itemDTOs) {
        return Stream.of(itemDTOs).map(ItemMapper::map).collect(Collectors.toList());
    }

    /**
     * Maps a requestItemDto to a list of items
     * @param items requestItemDto to be mapped to list of items
     * @return list of items
     */
    public static List<Item> map(RequestItemDto items) {
        List<Item> itemList = new LinkedList<>();
        for (int i = 0; i < items.amount(); i++) {
            itemList.add(new Item(
                    0,
                    LocalDate.parse(items.expirationDate(), formatter),
                    items.isDefected(),
                    new Branch(items.branch())
            ));
        }

        return itemList;
    }

}
