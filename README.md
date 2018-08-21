# Sunken

## Main
Authors: Digital, Node  
Copyright 2018 Sunken

#### File Structure
```bash
Main
│
├── bungeecord
│   └── Plugin for all bungeecords.
│
├── common
│   └── All servers (bungeecord, spigot) will include this module, used for global utils.
│
├── core
│   └── All spigot servers (lobbies, minigames) will include this module, used for spigot utils.
│
├── lib
│   └── Temporary maven repository for dependencies.
│
├── lobby
│   └── Lobby plugin for main lobbies and gamemode lobbies.
│
├── master
│   └── Master control handles large database calls and server scheduling.
│
└── minigameapi
    └── All minigames will include this module.
```
#### TO DO
##### Common
- [x] Server cache
- [x] Player cache
- [x] Achievements
- [x] Packet system
- [x] Database connections (redis, mongo)

##### BungeeCord
- [x] Lobby balancer (& command)
- [ ] Parties (Master as backend) 
- [ ] Friends (Master as backend) 
- [ ] Admin commands (banning, muting, kicking and /servers)

##### Lobby
- [ ] Main GUIs (Lobby selector, minigame selector)
- [x] Parkour (with synced leaderboard)
- [ ] Cosmetic menu (armorstand entities, hats, pets, items)
- [ ] Profile menu (achievements, cosmetic link, stats)
- [ ] Scoreboard

##### Minigame API
- [ ] Basic API for minigames to follow
