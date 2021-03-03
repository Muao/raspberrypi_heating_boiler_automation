на SD-карте с операционной системой Rapbian OS создать файл /boot/wpa_supplicant.conf;
скопировать в файл содержимое сниппета wpa_supplicant.conf;
значение <your_wifi_network_name> заменть на имя (ssid) вашей Wi-Fi сети;
значение <password_to_connect> заменть на пароль, который требуется для подключения к вашей Wi-Fi сети.
Вставить SD-карту в Raspberry Pi, и одноплатник подключится к Wi-Fi сразу после загрузки.

wpa_supplicant.conf

ctrl_interface=DIR=/var/run/wpa_supplicant GROUP=netdev
update_config=1
country=RU

network={
	ssid="<your_wifi_network_name>"
	psk="<password_to_connect>"
	key_mgmt=WPA-PSK
}



https://habr.com/ru/sandbox/148360/