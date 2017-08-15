

let msg = document.getElementById('message');
let btn = document.getElementsByName('send-btn')[0];
let chat = document.getElementById('chat');

const sendMessage = () => {
  console.log('Sending...');
  socket.send(msg.value);
}

const socket = new WebSocket('ws://localhost:5000/ws?foo=clojure');

msg.addEventListener("keyup", (event) => {
  event.preventDefault();
  if (event.keyCode == 13) {
    sendMessage();
  }
});

btn.onclick = () => sendMessage();

socket.onopen = (event) => console.log('Connection established...');

socket.onmessage = (event) => {

  let response = JSON.parse(event.data);

  if (response.key == 'chat') {
      var p = document.createElement('p');
      p.innerHTML = new Date().toLocaleString() + ":    " + response.data;
      chat.appendChild(p);
  }

}

socket.onclose = (event) => {
  if (event.wasClean) {
    console.log('Connection closed. Clean exit.')
  } else {
    console.log(`Code: ${event.code}, Reason: ${event.reason}`);
  }
}

socket.onerror = (event) => {
  console.log(`Error: ${event.message}`);
  socket.close();
}