// Modificar example/App.tsx para incluir o botão de obtenção de dados do dispositivo
import { useEvent } from 'expo';
import UrmoboModule, { UrmoboModuleView } from 'urmobo-module';
import { Button, SafeAreaView, ScrollView, Text, View, StyleSheet } from 'react-native';
import { useState } from 'react';

export default function App() {
  const onChangePayload = useEvent(UrmoboModule, 'onChange');
  const [deviceInfo, setDeviceInfo] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const handleGetDeviceInfo = async () => {
    try {
      setLoading(true);
      setError(null);
      const info = await UrmoboModule.getDeviceInfo();
      setDeviceInfo(info);
      console.log('Device Info:', info);
    } catch (err) {
      setError(err.message);
      console.error('Error getting device info:', err);
    } finally {
      setLoading(false);
    }
  };

  return (
    <SafeAreaView style={styles.container}>
      <ScrollView style={styles.container}>
        <Text style={styles.header}>Module API Example</Text>
        
        {/* Seção existente */}
        <Group name="Constants">
          <Text>{UrmoboModule.PI}</Text>
        </Group>
        <Group name="Functions">
          <Text>{UrmoboModule.hello()}</Text>
        </Group>
        <Group name="Async functions">
          <Button
            title="Set value"
            onPress={async () => {
              await UrmoboModule.setValueAsync('Hello from JS!');
            }}
          />
        </Group>
        <Group name="Events">
          <Text>{onChangePayload?.value}</Text>
        </Group>
        
        {/* Nova seção para Device Info */}
        <Group name="Device Info">
          <Button
            title={loading ? "Carregando..." : "Obter Informações do Dispositivo"}
            onPress={handleGetDeviceInfo}
            disabled={loading}
          />
          
          {error && (
            <Text style={styles.error}>Erro: {error}</Text>
          )}
          
          {deviceInfo && (
            <View style={styles.infoContainer}>
              <Text>IMEI: {deviceInfo.imei}</Text>
              <Text>Número de Série: {deviceInfo.serialNumber}</Text>
              <Text>ID do Dispositivo: {deviceInfo.deviceId}</Text>
            </View>
          )}
        </Group>
        
        <Group name="Views">
          <UrmoboModuleView
            url="https://www.example.com"
            onLoad={({ nativeEvent: { url } }) => console.log(`Loaded: ${url}`)}
            style={styles.view}
          />
        </Group>
      </ScrollView>
    </SafeAreaView>
  );
}

function Group(props: { name: string; children: React.ReactNode }) {
  return (
    <View style={styles.group}>
      <Text style={styles.groupHeader}>{props.name}</Text>
      {props.children}
    </View>
  );
}

const styles = StyleSheet.create({
  header: {
    fontSize: 30,
    margin: 20,
  },
  groupHeader: {
    fontSize: 20,
    marginBottom: 20,
  },
  group: {
    margin: 20,
    backgroundColor: '#fff',
    borderRadius: 10,
    padding: 20,
  },
  container: {
    flex: 1,
    backgroundColor: '#eee',
  },
  view: {
    flex: 1,
    height: 200,
  },
  error: {
    color: 'red',
    marginTop: 10,
  },
  infoContainer: {
    marginTop: 10,
    padding: 10,
    backgroundColor: '#f0f0f0',
    borderRadius: 5,
  }
});