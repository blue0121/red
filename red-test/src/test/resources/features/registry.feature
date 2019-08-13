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

  Scenario: registry watch
    Given start registry client "client1"
    And registry client "client1" "bind" "host1:1000"
    And registry client "client1" watch:
      | name  |
      | name1 |
      | name2 |
    When registry client "client1" "save":
      | name  |
      | name1 |
      | name2 |
    And sleep 1 seconds
    Then verify registry client "client1" can receive:
      | name  | host       |
      | name1 | host1:1000 |
      | name2 | host1:1000 |

   Scenario: registry watch with multi client
     Given start registry client "client1"
     And registry client "client1" "bind" "host1:1000"
     And start registry client "client2"
     And registry client "client2" "bind" "host2:1000"
     And start registry client "client3"
     And registry client "client3" watch:
       | name  |
       | name1 |
       | name2 |
     When registry client "client1" "save":
       | name  |
       | name1 |
       | name2 |
     And registry client "client2" "save":
       | name  |
       | name1 |
       | name2 |
     And sleep 1 seconds
     Then verify registry client "client3" can receive:
       | name  | host       |
       | name1 | host1:1000, host2:1000 |
       | name2 | host1:1000, host2:1000 |
     And clear registry client "client3"
     When registry client "client2" "unbind" "host2:1000"
     And sleep 1 seconds
     Then verify registry client "client3" can receive:
       | name  | host       |
       | name1 | host1:1000 |
       | name2 | host1:1000 |
     And clear registry client "client3"
     When registry client "client1" "unbind" "host1:1000"
     And sleep 1 seconds
     Then verify registry client "client3" can receive:
       | name  | host       |
       | name1 |            |
       | name2 |            |
     And clear registry client "client3"

  Scenario: registry watch with multi client2
    Given start registry client "client1"
    And registry client "client1" "bind" "host1:1000"
    And start registry client "client2"
    And registry client "client2" "bind" "host2:1000"
    And start registry client "client3"
    And registry client "client3" watch:
      | name  |
      | name1 |
      | name2 |
    When registry client "client1" "save":
      | name  |
      | name1 |
      | name2 |
    And registry client "client2" "save":
      | name  |
      | name1 |
      | name2 |
    And sleep 1 seconds
    Then verify registry client "client3" can receive:
      | name  | host       |
      | name1 | host1:1000, host2:1000 |
      | name2 | host1:1000, host2:1000 |
    And clear registry client "client3"
    When stop registry client "client2"
    And sleep 1 seconds
    Then verify registry client "client3" can receive:
      | name  | host       |
      | name1 | host1:1000 |
      | name2 | host1:1000 |
    And clear registry client "client3"
    When stop registry client "client1"
    And sleep 1 seconds
    Then verify registry client "client3" can receive:
      | name  | host       |
      | name1 |            |
      | name2 |            |
    And clear registry client "client3"

