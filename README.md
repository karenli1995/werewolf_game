To run:
mvn spring-boot:run

Access the application locally at: 
localhost:8080


Source:
I followed a basic tutorial to understand basics of Web Socket programming using Spring Boot framework at this link: https://www.callicoder.com/spring-boot-websocket-chat-example/. The tutorial provided a very basic chat application that only creates a single chat room.

I built upon this example by adding the additional ability to host and join multiple chat rooms, and also implemented validation features described below.


Features:
1. Host a chatroom. Validates that the host is creating a NEW chat room with a new ID. Every time a host creates a new chatroom, this information is broadcast to all clients in different chat rooms (requirement #1).

2. Join an existing chatroom (requirement #2 and #4). Validates that a user is joining an EXISTING chat room.

3. Chatrooms display notifications about users joining a room, leaving a room, and the creation of new chat rooms. (requirement #5). Chatrooms display messages from users in a specific chat room (requirement #3).



Known bugs:
1. Does not display when a host has left a meeting.