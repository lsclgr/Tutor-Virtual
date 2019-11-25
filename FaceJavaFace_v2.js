'use strict';


var net = require('net');
// Imports dependencies and set up http server

var resposta_user="";
const 
  request = require('request'),
  express = require('express'),
  body_parser = require('body-parser'),
  app = express().use(body_parser.json()); // creates express http server

// Sets server port and logs message on success
app.listen(process.env.PORT || 1337, () => console.log('webhook is listening'));

app.get('/webhook', (req, res) => {
  
  /** UPDATE YOUR VERIFY TOKEN **/
  const VERIFY_TOKEN = "TutorVirtualKawaii";
  
  // Parse params from the webhook verification request
  let mode = req.query['hub.mode'];
  let token = req.query['hub.verify_token'];
  let challenge = req.query['hub.challenge'];
    
  // Check if a token and mode were sent
  if (mode && token) {
  
    // Check the mode and token sent are correct
    if (mode === 'subscribe' && token === VERIFY_TOKEN) {
      
      // Respond with 200 OK and challenge token from the request
      console.log('WEBHOOK_VERIFIED');
      res.status(200).send(challenge);
    
    } else {
      // Responds with '403 Forbidden' if verify tokens do not match
      res.sendStatus(403);      
    }
  }
});
app.post('/webhook', (req, res) => {
  var data=req.body;

  if(data && data.object==='page'){
    //percorrer todas as entradas entry
    data.entry.forEach(function(entry){
      var pageID=entry.id;
      var timeOfEvent=entry.time;
      
      //percorrer todas as mensagens
      entry.messaging.forEach(function(event){
      console.log('chegou event');
        if(event.message){
         console.log(event.message.text);
      // Get the sender PSID
      let sender_psid = event.sender.id;
        console.log('Sender PSID: ' + sender_psid);
        
      // Check if the event is a message or postback and
      // pass the event to the appropriate handler function
      if (event.message) {
        if(sender_psid!=192563598063518){
          enviaServidor(event.message.text,sender_psid, event.message);
        }
        console.log("aqui");
        //handleMessage(sender_psid, event.message);  
        //console.log(respostaServidor);
        res.status(200).send("Saulo\n");      
      } else if (event.postback) {
        handlePostback(sender_psid, event.postback);
      }
        }
      })
    })
    //res.end("teste");
    //res.sendStatus(200);
    //res.status(200).send("\n");
  }
  
});

  function handleMessage(sender_psid, received_message) {
  
  let response;

  // Check if the message contains text
  if (received_message.text) {    

    // Create the payload for a basic text message
   /* response = {
      "text": `Mensagem recebida pelo Tutor: "${received_message.text}".`
    */
   console.log(received_message.text);
   if (received_message.text=="Write Question") {
    response = {
      "text":`${resposta_user}`,
      "quick_replies":[
        {
          "content_type":"text",
          "title":"1",
          "payload":"1\n"
        },{
          "content_type":"text",
          "title":"2",
          "payload":"2\n"
        },{
          "content_type":"text",
          "title":"3",
          "payload":"3\n"
        }
      ]
    }

   }else{

    response = {
        "text":`${resposta_user}`,
        "quick_replies":[
          {
            "content_type":"text",
            "title":"Write Question",
            "payload":"Write Question\n"
          },{
            "content_type":"text",
            "title":"Score",
            "payload":"Score\n"
          }
        ]
  }
}
}
  // Sends the response message
  callSendAPI(sender_psid, response,received_message);    
}
function callSendAPI(sender_psid, response,received_message) {
  // Construct the message body
  let request_body = {
    "recipient": {
      "id": sender_psid
    },
    "message": response
  }
  console.log(received_message.mid);
  // Send the HTTP request to the Messenger Platform
  request({
    "uri": "https://graph.facebook.com/v4.0/me/messages",
    "qs": { "access_token": 'EAAJjZCJ0pOtIBACYeIpfyBgsxRIa8Q3Rh8WKKBwmp1gga0hNInw3PCMtIZCJbTbAzJYHhedrpT4X53GGSZC1fefVbKMzKFjemLA9zZBR1nHG2oYeqbf676KD7gjXQlSPdrrk3UUnmQqASnBn5CExbnz3gbEZAZBKLqET1zcpDZCOiZAz4B8xY8477qkbjZCP4lLIZD'},
    "method": "POST",
    "json": request_body
  }, (err, res, body) => {
    if (!err) {
      console.log('message sent!')
    } else {
      console.error("Unable to send message:" + err);
    }
  }); 
  console.log("enviado...");
}

var respostaServidor;


// This function create and return a net.Socket object to represent TCP client.
function criaSocket(sender_psid, received_message){

    // Create TCP client.
    var client = net.createConnection(1010,'localhost');

    client.setEncoding('utf8');
    client.setKeepAlive(true, 1000);

    // Dados são retornados para o socket
    client.on('data', function (data) {
        console.log('Dado do servidor : ' + data);
    respostaServidor=data;
    resposta_user=respostaServidor;
    handleMessage(sender_psid, received_message);
    });

    //Conexão encerrada aqui
    client.on('end',function () {
        //console.log('Fechando o socket aqui');
        console.log('------------------------')
    });

    client.on('error', function (err) {
        console.log("Verifique o Servidor Java...\n")
        console.error(JSON.stringify(err));
    });

    return client;
}
function enviaServidor(message, sender_psid, received_message){
  // Create a java client socket.
  var javaClient = criaSocket(sender_psid, received_message);

  javaClient.write(sender_psid+"<>"+message+'\r\n');

}