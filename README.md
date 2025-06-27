# Módulo Urmobo

Um módulo nativo Expo para integração com o sistema Urmobo MDM para obtenção de informações do dispositivo.

## Visão Geral

Este módulo foi desenvolvido para facilitar a comunicação entre aplicativos React Native/Expo e o sistema Urmobo MDM (Mobile Device Management). Ele permite recuperar informações importantes do dispositivo como IMEI, número de série e ID do dispositivo.

### Estrutura do Projeto

O módulo foi recriado como uma pasta independente modules/urmobo-module com a seguinte estrutura:

    modules/urmobo-module/
    ├── android/                      # Implementação nativa para Android
    │   └── src/main/java/com/megaappgnos/urmobo/
    │       └── UrmoboModule.kt       # Implementação em Kotlin
    ├── plugin/                       # Plugin de configuração
    │   ├── build.js                  # Script de build
    │   └── src/
    │       └── index.ts              # Implementação do plugin
    ├── src/                          # Código fonte TypeScript
    ├── expo-module.config.json       # Configuração do módulo
    └── index.ts                      # Ponto de entrada

### Funcionalidades

O módulo implementa as seguintes funcionalidades:

Envio de Intent para o Urmobo MDM: Envia uma intent com a ação com.urmobo.mdm.GET_DEVICE_DATA para solicitar informações do dispositivo.

Recebimento de Resposta: Configura um BroadcastReceiver para capturar a resposta com a ação com.cliente.RESPONSE_INFO.

Processamento de Dados: Extrai as informações do dispositivo (IMEI, número de série e ID) da resposta recebida.

Exposição para JavaScript: Disponibiliza os dados para a camada JavaScript do React Native através de uma API simples.

### Config Plugin

Foi adicionado um Config Plugin que modifica automaticamente o AndroidManifest.xml para incluir os filtros de intent necessários para a comunicação com o Urmobo MDM. Isso elimina a necessidade de modificar manualmente os arquivos nativos.

Para usar o plugin, adicione-o à configuração do seu aplicativo Expo:

    // app.config.js ou app.json
    export default {
      // ...outras configurações
      plugins: [
        // ...outros plugins
        "./modules/urmobo-module/plugin"
      ]
    };

## Como Usar

### Instalação

Se estiver usando como módulo local
npm install ./modules/urmobo-module
Exemplo de Uso
import { getDeviceInfo } from 'urmobo-module';

    async function fetchDeviceInfo() {
      try {
        const deviceInfo = await getDeviceInfo();
        console.log('IMEI:', deviceInfo.imei);
        console.log('Número de Série:', deviceInfo.serialNumber);
        console.log('ID do Dispositivo:', deviceInfo.deviceId);
      } catch (error) {
        console.error('Erro ao obter informações do dispositivo:', error);
      }
    }

**Testando com ADB**
Para testar o módulo sem um sistema Urmobo MDM real, você pode simular a resposta usando o ADB (Android Debug Bridge):

    adb shell am broadcast -a com.cliente.RESPONSE_INFO --es UrmoboID "test_id" --es IMEI "test_imei" --es SerialNumber "test_serial"

Este comando simula uma resposta do sistema Urmobo MDM com os seguintes dados:

    UrmoboID: "test_id"
    IMEI: "test_imei"
    SerialNumber: "test_serial"

**Fluxo de Comunicação**
O aplicativo chama a função _getDeviceInfo()_
O módulo envia uma intent para o Urmobo MDM: com.urmobo.mdm.GET_DEVICE_DATA
O Urmobo MDM processa a solicitação e envia uma resposta via intent: com.cliente.RESPONSE_INFO
O módulo captura essa resposta através do BroadcastReceiver configurado
Os dados são extraídos e retornados para o JavaScript como uma Promise

### Requisitos

React Native / Expo SDK 51 ou superior
Android API Level 21 ou superior
Repositório

Para mais informações, visite o repositório do projeto: https://github.com/hiroaka/urmobo-module/

Licença
MIT
