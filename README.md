# DVRS — Distance Vector Routing Simulator (Java)

A small **Distance Vector (DV) routing** simulator (Bellman–Ford style) written in Java. It models a fixed network topology, lets nodes exchange distance vectors via simulated packet delivery, and prints each node’s final **distance vector** and **forwarding table** once the system converges.

## What this project demonstrates
- **Distance Vector routing** updates via: DV[d] = minₙ { cost(me,n) + DVₙ[d] }
- **Asynchronous message passing** (events + packets)
- **Convergence behavior**: number of updates each node needed until stable
- **Forwarding table derivation** from the learned best next-hop choices

## Project structure
- `src/Project.java`: program entrypoint (`main`)
- `src/DVSimulator.java`: simulation driver, topology (`neighbors`) and link costs (`cost`)
- `src/Node.java`: DV logic (initialize DV, send updates, process received DVs, build forwarding table)
- `src/Event.java`, `src/EventList.java`, `src/EventListImpl.java`: event queue
- `src/Packet.java`: packet model (source, destination, distance vector payload)
