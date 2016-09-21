Peer-to-Peer Communication Across Network Address Translators
============================================
*A NAT Traversal Mechanism for Peer-To-Peer Networks: UDP Hole Punching Implementation*

Peer-to-peer is a type of network communication in which a group of computers communicate directly with each other, rather than through a central server. P2P applications are very popular because they don’t require use of third party servers to forward traffic between them. However, this style of networking often has problems dealing with Network Address Translators (NATs). UDP Hole punching is a way to solve these problems.

1. UDP Hole punching
===================
UDP hole punching enables two clients to set up a direct peer-to-peer UDP session with the help of a well-known rendezvous point (server), even if the clients are both behind different NATs.

1.1. How it Works
-------------------------

Assume that Alice and Bob are two hosts that want to communicate each other behind NAT and a third system which helps them in establishing a connection is S.

1. Both Alice and Bob send UDP packets to the server S. When packets reach the NAT devices of the Alice and Bob, NAT devices rewrite the source IP address with their globally accessible IP addresses.

2. Server S examines the UDP packets to learn source IP addresses (IPa and IPb) and port numbers (Pa and Pb) of the clients.

3. Then S tells Alice to send UDP packet to IPb with port number Pb and tells Bob to send UDP packet to IPa with port Pa.

4. When Alice sends a packet to IPb at port Pb, NAT device of A inserts a record into its table and allows incoming packets from the globally reachable IP address of Bob (IPb) at port Pb. Same thing happens with NAT device of Bob and it also allows incoming packets from the IPa at Pa. This causes everyone's NAT to open up a bidirectional hole for the UDP traffic to go through. As a result, a P2P connection is established between Alice and Bob. Sometimes UDP Hole Punching may not be possible due to various reasons like port randomization by the NAT. In the cases where UDP hole punching is not possible.

**Common Problems Encountered with UDP Hole Punching**

1. Both hosts Alice and Bob need to know public IP address of the server S.

2. The firewall at the NAT might not allow incoming UDP connections from outside.

3. When Alice sends a packet, then it is possible that the NAT changes the port number of Alice in the outgoing packet (it is called port randomization). This makes UDP hole punching almost impossible.

1.2. Implementation Details
----------------------------------------

A client side application and a server side application which provide P2P instant messaging have been developed. Both of these have been implemented using Java multi-thread programming.

**1.2.1. Server**

Server listens to a UDP socket which is bound to a specific port number. When a packet that contains message `<Hi!>` comes from a client, its IP address and port number has been stored. Then server starts a new thread and echoes the same message with uppercase `<HI!>` to the client. If a client wants to start P2P connection with other peers, it sends a connection request to the server and if there is more than one client stored in the list at the server, server responds a message to the client that contains other peer’s globally accessible IP addresses, private IP address and port numbers. The packet is sent by server also contains the private IP addresses of clients because clients might behind the same NAT.

Thus, every client in the list knows other clients IP addresses and port numbers. So they can establish P2P connection with each other.

**1.2.2. Client**

When client program starts, it sends a packet to the server that contains message `<Hi!>`. If server echoes the same message, client knows that the server is running and it’s available to handle connection requests.

If a client wants to start P2P connection with other peers, it sends a connection request to the server and if there is more than one client stored in the list at the server, server responds a message to the client that contains other peer’s globally accessible IP addresses, private IP address and port numbers. Then each client starts a new thread and sends UDP packets which contain command line inputs (System.in) to other peers as a part of instant messaging application.

Since nobody knows at first whether they are behind the same NAT, the first packet is always sent to both the public and the private address. Once the first reply comes back from each peer, the sender knows which return address to use, and the sender can stop sending to both addresses. While clients send packets each other, at the same time another thread receives incoming packets and display messages on the screen.

**SKYPE also uses UDP Hole Punching**

When two machines running Skype need to communicate directly, but are both behind a NAT, they use UDP Hole Punching technique. However, Skype uses computers of its users to act as a Server. Any client who has a globally reachable IP address can become the third party Server. 

Assume that Alice wants to call Bob. Skype server knows what addresses Alice and Bob are currently available on the internet but the telephone connections do not run via the Skype server, instead, the clients exchange data directly.

First Alice tells the Skype server that she wants to call Bob. Then, Skype server passes Alice’s information (IP address and port number) to Bob. Bob's Skype program punches a hole in its own network. It sends a UDP packet to Alice. But Alice’s firewall drops the packet. Now there is a hole in Bob’s firewall and it allows packets which come from Alice’s IP and port number. Then server passes Bob’s information to Alice and Alice punches a hole in its own network for Bob. By this way, they communicate peer-to-peer.

![Figure 1: UDP Hole Punching](https://i.imgsafe.org/27679e2fb3.png)

If UDP Hole punching is not possible because of the port randomization by the NAT devices, then the server is used to pass the whole communication between users behind NAT.
