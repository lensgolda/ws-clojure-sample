
const socket = new WebSocket('ws://localhost:5000/ws?type=chat');

let msg = document.getElementById('message');
let btn = document.getElementsByName('send-btn')[0];
let chat = document.getElementById('chat');

const sendMessage = () => {
  socket.send(msg.value);
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
  let p = document.createElement('p');
  let htmlText = new Date().toLocaleString() + ":    " + response.message;

  if (response.key == 'chat') {
      p.innerHTML = htmlText;
      chat.appendChild(p);
  } else if (response.key == 'data') {

    let responses = response.data;

    responses.forEach(resp => {
        let keys = Object.keys(resp);
        switch (keys.length) {
            case 5:
                htmlText += ("<br>    User-Agent:    " + resp.headers["User-Agent"]);
                break;
            case 2:
                htmlText += ("<br>    NOW:    " + resp.now.rfc2822);
                break;
            case 1:
                htmlText += ("<br>    IP:    " + resp.origin);
                break;
        }
    });
    p.innerHTML = htmlText;
    chat.appendChild(p);
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