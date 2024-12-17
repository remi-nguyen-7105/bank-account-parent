package kata.bank.account.service;

import java.util.Optional;

public interface Repository<K, V> {
    Optional<V> findById(K key);
}
