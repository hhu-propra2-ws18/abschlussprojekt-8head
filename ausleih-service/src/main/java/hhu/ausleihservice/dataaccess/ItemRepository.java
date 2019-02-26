package hhu.ausleihservice.dataaccess;

import hhu.ausleihservice.databasemodel.Item;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
public interface ItemRepository extends ItemBaseRepository<Item> {

}
