/* A simple server in the internet domain using TCP
   The port number is passed as an argument */
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <sys/types.h> 
#include <sys/socket.h>
//used for internet domain address
#include <netinet/in.h>

#define TAILLE_MAXI 1024

int comp(char*, char*);
int init_socket_server(int);
void envoi_fichier_par_socket(char*, int);
void reception_fichier_par_socket(char*);

//envoie de message en cas d'erreur
void error(const char *msg)
{
    perror(msg);
    exit(1);
}

int main(int argc, char *argv[])
{
  //sockfd and newsockfd are file descriptor, store the value of the socket system call and the accept system call
  //portno registers the port number accepted by the server
  int sockfd, newsockfd;
  socklen_t clilen;
  struct sockaddr_in cli_addr;
  //the server read the information into this buffer
  char buffer[TAILLE_MAXI];
  //n is the returned value for the read and write call, the number of characters read or written     
  int n;
  
  if (argc < 2) {
      fprintf(stderr,"ERROR, no port provided\n");
      exit(1);
  }
  sockfd = init_socket_server(atoi(argv[1]));     
  

  //accepting procedure 
  clilen = sizeof(cli_addr);
  newsockfd = accept(sockfd, 
		    (struct sockaddr *) &cli_addr, 
		    &clilen);
  if (newsockfd < 0) 
      error("ERROR on accept");

	
  bzero(buffer,TAILLE_MAXI);
  //reading from the socket
  n = read(newsockfd,buffer,(TAILLE_MAXI)-1);
  if (n < 0) error("ERROR reading from socket");
  printf("Here is the message: %s\n",buffer);

  envoi_fichier_par_socket("example.xml", newsockfd);
  //close(newsockfd);
  //boucle sur l'envoi de testMAJ.xml
  int cont = 1;
  while(cont)
  {
     /*newsockfd = accept(sockfd, 
		    (struct sockaddr *) &cli_addr, 
		    &clilen);
    if (newsockfd < 0) 
	error("ERROR on accept");*/

	  
    bzero(buffer,TAILLE_MAXI);
    //reading from the socket
    n = read(newsockfd,buffer,(TAILLE_MAXI)-1);
    if (n < 0) error("ERROR reading from socket");
    printf("Here is the message: %s\n",buffer);
    char* ligne1 = strtok(buffer, "\n");
    if(comp(ligne1, "stop"))
          cont = 0;
    else if(comp(ligne1, "send"))
      envoi_fichier_par_socket("testMAJ.xml", newsockfd);
    else if(comp(ligne1, "receive"))
      reception_fichier_par_socket("testEnvoiModif.txt");
    //c'est assez moche de fermer et ouvrir à chaque fois, voir si on peut faire autrement
    //close(newsockfd);
  }
  close(sockfd);
  return 0; 
}

int comp(char* s1, char* s2)
{
  printf("s1: %d\n", (int) strlen(s1));
  printf("s2: %d\n", (int) strlen(s2));
  if(strlen(s1) != strlen(s2)) return 0;
  else
  {
    int i;
    for(i=0; i < strlen(s1); i++)
    {
      if(s1[i] != s2[i]) return 0;
    }
    return 1;
  }
}

int init_socket_server(int port)
{
  int sockfd;
  
  //structure containing an internet address
  struct sockaddr_in serv_addr;

  //explicit
  
  //0 pour le protocole le plus adapté (TCP/UDP)
  //SOCK_STREAM -> type de socket (there continuous stream)
  //AF_INET -> address domain of the socket
  sockfd = socket(AF_INET, SOCK_STREAM, 0);
  //if socket call fails it returns 0
  if (sockfd < 0) 
        error("ERROR opening socket");


  //bzero sets all values in the buffer to zero
  bzero((char *) &serv_addr, sizeof(serv_addr));
 
  serv_addr.sin_family = AF_INET;
  //IP address of the host
  serv_addr.sin_addr.s_addr = INADDR_ANY;
  serv_addr.sin_port = htons(port);

  //binding operation of the socket with the address of the server
  //attention operation de cast parce qu'ici on envoie une structure sockaddr_in
  if (bind(sockfd, (struct sockaddr *) &serv_addr,
           sizeof(serv_addr)) < 0) 
           error("ERROR on binding");
  //5-> number of connections that can be waiting while the server is handling one process
  //listening for potential connection
  //first argument is the socket file descriptor
  listen(sockfd,5);
  return sockfd;
}

void envoi_fichier_par_socket(char* nomFichier, int socket_client)
{
  char buffer[TAILLE_MAXI];
  int n;
  FILE *fichier = fopen (nomFichier, "r");
    //------------------------------------------------------------------
    if (fichier != NULL)
    {
	  bzero(buffer,TAILLE_MAXI);
		
	  while(fgets(buffer, TAILLE_MAXI, fichier)!=NULL){
	  //fgets(chaine, TAILLE_MAXI, fichier); // On lit maximum TAILLE_MAX caractères du fichier, on stocke le tout dans "chaine"
		n = write(socket_client, buffer, strlen(buffer));
		if (n<0) error("ERROR writing to socket");
		printf("%s", buffer);
		bzero(buffer,TAILLE_MAXI);
	  }
	  fclose (fichier);
	  //!!"\0" est indispensable: sans cela, le client ne détectera pas la fin du flux et restera en attente sur le  dernier caractère
	  n = write(socket_client, "\0", 1);
		if (n<0) error("ERROR writing to socket");
    }
    else
    {
      printf ("Erreur d'ouverture du fichier\n");
    }
}

/**
 * écrit dans un fichier la mise à jour reçue.
 * on suppose que strtok a été appelé une fois auparavant
 */
void reception_fichier_par_socket(char* nomFichier)
{
  char buffer[TAILLE_MAXI];
  int n;
  FILE *fichier = fopen (nomFichier, "w");
    //------------------------------------------------------------------
  if(fichier != NULL)
  {
    char* ligne = strtok(NULL, "\n");
    while(!comp(ligne, "end"))
    {
      fprintf(fichier, "%s\n", ligne);
      ligne = strtok(NULL, "\n");
    }
    fclose(fichier);
  }
  else
  {
    printf ("Erreur d'ouverture du fichier\n");
  }
}