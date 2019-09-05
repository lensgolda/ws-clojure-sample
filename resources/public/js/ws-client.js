
const socket = new WebSocket('ws://localhost:5000/ws');

let msg = document.getElementById('message');
let btn = document.getElementsByName('send-btn')[0];
let chat = document.getElementById('chat');
let fakeName = faker.fake("{{name.firstName}}");

const sendMessage = () => {
  let data = JSON.stringify({message: msg.value, name: fakeName});
  socket.send(data);
}

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
  /* COMMENT ME */
  console.log(response);
  let p = document.createElement('p');
  let htmlText = new Date().toLocaleString();
  htmlText += " " + response.name;
  htmlText += ":    " + response.message;

  if (response.key == 'chat') {
      p.innerHTML = htmlText;
      chat.appendChild(p);
  } else if (response.key == 'data') {

    let responses = response.data;

    responses.forEach(resp => {
        let p = document.createElement('p');
        p.innerHTML = "<pre>" + JSON.stringify(resp) + "</pre>";
        chat.appendChild(p);
    });
  } else {
    console.log(response.data);
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