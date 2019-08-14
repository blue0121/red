Feature: cache service

  Scenario: cache save and delete
    Given start cache client "client1"
    When cache client "client1" "set":
      | key  | value  | ttl |
      | key1 | value1 | 0   |
      | key2 | value2 | 0   |
    Then cache client "client1" "get":
      | key  | value  | ttl |
      | key1 | value1 | 0   |
      | key2 | value2 | 0   |
    When cache client "client1" "delete":
      | key  | value  | ttl |
      | key1 | value1 | 0   |
    Then cache client "client1" "get":
      | key  | value  | ttl |
      | key2 | value2 | 0   |
    When cache client "client1" "delete":
      | key  | value  | ttl |
      | key2 | value2 | 0   |
    Then cache client "client1" "get":
      | key  | value  | ttl |
      | key2 | (null) | 0   |
