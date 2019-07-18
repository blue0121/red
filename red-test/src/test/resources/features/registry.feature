Feature: registry service

  Scenario: registry save and delete
    Given start registry client "client1"
    And registry client "client1" "bind" "host1:1000"
    When registry client "client1" "save":
      | name  |
      | name1 |
      | name2 |
    Then registry client "client1" "get":
      | name  | host       |
      | name1 | host1:1000 |
      | name2 | host1:1000 |
    When registry client "client1" "delete":
      | name  |
      | name1 |
    Then registry client "client1" "get":
      | name  | host       |
      | name1 |            |
      | name2 | host1:1000 |
    When registry client "client1" "delete":
      | name  |
      | name2 |
    Then registry client "client1" "get":
      | name  | host       |
      | name1 |            |
      | name2 |            |

  Scenario: registry save exception
    Given start registry client "client1"
    When registry client "client1" "save":
      | name  |
      | name1 |
    Then verify registry client "client1" throw exception "com.red.client.registry.RegistryClientException"

