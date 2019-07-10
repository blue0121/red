Feature: registry service

  Scenario: registry save and delete
    Given start registry client "client1"
    When registry client "client1" "save":
      | name  | host       |
      | name1 | host1:1000 |
      | name2 | host1:1000 |
    Then registry client "client1" get:
      | name  | host       |
      | name1 | host1:1000 |
      | name2 | host1:1000 |
    When registry client "client1" "delete":
      | name  | host       |
      | name1 | host1:1000 |
    Then registry client "client1" get:
      | name  | host       |
      | name2 | host1:1000 |
    When registry client "client1" "delete":
      | name  | host       |
      | name2 | host1:1000 |
    Then registry client "client1" get:
      | name  | host       |
      | name2 |            |

  Scenario: registry watch
    Given start registry client "client1"
    And registry client "client1" watch:
      | name  |
      | name1 |
      | name2 |
    When registry client "client1" "save":
      | name  | host       |
      | name1 | host1:1000 |
      | name2 | host1:1000 |
    And sleep 2 seconds
    Then verify registry client "client1" can receive:
      | name  | host       |
      | name1 | host1:1000 |
      | name2 | host1:1000 |
    And clear registry client "client1"
    When registry client "client1" "delete":
      | name  | host       |
      | name1 | host1:1000 |
    And sleep 2 seconds
    Then verify registry client "client1" can receive:
      | name  | host       |
      | name2 | host1:1000 |
    Then verify registry client "client1" can not receive:
      | name  | host       |
      | name1 | host1:1000 |
    And clear registry client "client1"


