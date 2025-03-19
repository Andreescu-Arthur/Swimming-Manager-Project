This project is a simple implementation of a swimming competition registration system. It includes model classes for participants, races, and registrations, along with a generic repository interface for data management.
Participants: Manage participant details (name and age).

Races: Manage race details (distance, style, and total participants).

Registrations: Link participants to races.

Repository Interface: Provides basic CRUD operations.



Data base schema:
+-----------------+          +------------------+           +---------------------+
|  Participants   |          |  Registrations   |           |        Races        |
+-----------------+          +------------------+           +---------------------+
| participant_id  |◄───────┐  | registration_id  |          | distance            |
| name            |        ├──► participant_id  ├           | style               |
| age             |           | race_id         ┤──────────►┤ race_id             |
+-----------------+           +------------------+          | total_participants  |
                                                             +--------------------+
