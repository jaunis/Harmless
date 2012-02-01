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

#define TAILLE_MAXI 256

int comp(char*, char*);

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

     int sockfd, newsockfd, portno;
     socklen_t clilen;
//the server read the information into this buffer
     char buffer[TAILLE_MAXI];
//structure containing an internet address
     struct sockaddr_in serv_addr, cli_addr;
//n is the returned value for the read and write call, the number of characters read or written     
     int n;
//explicit
     if (argc < 2) {
         fprintf(stderr,"ERROR, no port provided\n");
         exit(1);
     }
//0 pour le protocole le plus adapté (TCP/UDP)
//SOCK_STREAM -> type de socket (there continuous stream)
//AF_INET -> address domain of the socket
     sockfd = socket(AF_INET, SOCK_STREAM, 0);
//if socket call fails it returns 0
     if (sockfd < 0) 
        error("ERROR opening socket");


//bzero sets all values in the buffer to zero
     bzero((char *) &serv_addr, sizeof(serv_addr));
//cast du port de char en integer
     portno = atoi(argv[1]);
     serv_addr.sin_family = AF_INET;
//IP address of the host
     serv_addr.sin_addr.s_addr = INADDR_ANY;
     serv_addr.sin_port = htons(portno);

//binding operation of the socket with the address of the server
//attention operation de cast parce qu'ici on envoie une structure sockaddr_in
     if (bind(sockfd, (struct sockaddr *) &serv_addr,
              sizeof(serv_addr)) < 0) 
              error("ERROR on binding");
//5-> number of connections that can be waiting while the server is handling one process
//listening for potential connection
//first argument is the socket file descriptor
     listen(sockfd,5);

//accepting procedure 
     clilen = sizeof(cli_addr);
     int cont = 1;
     while(cont)
     {
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
	if(!comp(buffer, "send")) cont = 0;


      //fichier à changer, ce sera le .xml à placer dans le même fichier
      //------------------------------------------------------------------
	      FILE *fichier = fopen ("example.xml", "r");
      //------------------------------------------------------------------



	if (fichier != NULL)
	{
	      bzero(buffer,TAILLE_MAXI);
	      
	      while(fgets(buffer, TAILLE_MAXI, fichier)!=NULL){
	      //fgets(chaine, TAILLE_MAXI, fichier); // On lit maximum TAILLE_MAX caractères du fichier, on stocke le tout dans "chaine"
		      n = write(newsockfd, buffer, strlen(buffer));
		      if (n<0) error("ERROR writing to socket");
		      printf("%s", buffer);
		      bzero(buffer,TAILLE_MAXI);
	      }
	  
	    fclose (fichier);
	}
	else
	{
	    printf ("Erreur d'ouverture du fichier\n");
	}


	  //n = write(newsockfd,"I got your message",18);
	  if (n < 0) error("ERROR writing to socket");
	  close(newsockfd);

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