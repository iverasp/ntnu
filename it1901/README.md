##Sette opp git for bruk med GitHub
Se http://help.github.com/set-up-git-redirect

##Sette opp Vagrant (OSX)

[Installer VirtualBox](https://www.virtualbox.org/wiki/Downloads)  
[Installer Vagrant](http://vagrantup.com/)  

git clone git@github.com:iverasp/prosjekt1.git  
cd prosjekt1  
vagrant init  
vagrant box add precise32 http://files.vagrantup.com/precise32.box  
endre linje 13 i VagrantFile til "config.vm.box = "precise32""  
vagrant up  
