Vagrant.configure("2") do |config|
  config.vm.box = "ubuntu/trusty64"

  config.vm.network "forwarded_port", guest: 8888, host: 8888

   config.vm.provider "virtualbox" do |vb|
     vb.customize ["modifyvm", :id, "--natdnshostresolver1", "on"]
     vb.memory = "2048"
   end
   
   config.vm.provision "shell", path: "provision.sh" 

end
