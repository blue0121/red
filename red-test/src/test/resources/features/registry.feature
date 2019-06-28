Feature: registry service

  Scenario: registry save and delete
    Given start registry client "client1"
    When registry client "client1" "save":
      | name  | host       |
      | name1 | host1:1000 |
      | name2 | host2:1000 |
    Then registry client "client1" get:
      | name  | host       |
      | name1 | host1:1000 |
      | name2 | host2:1000 |
    When registry client "client1" "delete":
      | name  | host       |
      | name1 | host1:1000 |
    Then registry client "client1" get:
      | name  | host       |
      | name2 | host2:1000 |
    When registry client "client1" "delete":
      | name  | host       |
      | name2 | host2:1000 |
    Then registry client "client1" get:
      | name  | host       |
      | name2 |            |



