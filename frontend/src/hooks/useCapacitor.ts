import { App } from '@capacitor/app';
import { Haptics, ImpactStyle } from '@capacitor/haptics';
import { Camera, CameraResultType, CameraSource } from '@capacitor/camera';
import { Storage } from '@capacitor/storage';
import { Keyboard } from '@capacitor/keyboard';
import { StatusBar } from '@capacitor/status-bar';
import { PushNotifications } from '@capacitor/push-notifications';
import { useEffect, useState } from 'react';
import { isPlatform } from '@capacitor/core';

export const useCapacitor = () => {
  const [isNative, setIsNative] = useState(false);

  useEffect(() => {
    setIsNative(isPlatform('ios') || isPlatform('android'));
  }, []);

  const takePicture = async () => {
    try {
      const image = await Camera.getPhoto({
        quality: 90,
        allowEditing: true,
        resultType: CameraResultType.Base64,
        source: CameraSource.Camera
      });
      return image;
    } catch (error) {
      console.error('Error taking picture:', error);
      return null;
    }
  };

  const vibrate = async () => {
    if (isNative) {
      await Haptics.impact({ style: ImpactStyle.Light });
    }
  };

  const storeData = async (key: string, value: string) => {
    await Storage.set({ key, value });
  };

  const getData = async (key: string) => {
    const { value } = await Storage.get({ key });
    return value;
  };

  const setupPushNotifications = async () => {
    if (!isNative) return;

    let permStatus = await PushNotifications.checkPermissions();
    
    if (permStatus.receive === 'prompt') {
      permStatus = await PushNotifications.requestPermissions();
    }

    if (permStatus.receive !== 'granted') {
      throw new Error('User denied permissions!');
    }

    await PushNotifications.register();

    PushNotifications.addListener('pushNotificationReceived', notification => {
      console.log('Push notification received:', notification);
    });

    PushNotifications.addListener('pushNotificationActionPerformed', notification => {
      console.log('Push notification action performed:', notification);
    });
  };

  const setupKeyboard = async () => {
    if (!isNative) return;

    Keyboard.addListener('keyboardWillShow', info => {
      console.log('keyboard will show with height:', info.keyboardHeight);
    });

    Keyboard.addListener('keyboardDidShow', info => {
      console.log('keyboard did show with height:', info.keyboardHeight);
    });

    Keyboard.addListener('keyboardWillHide', () => {
      console.log('keyboard will hide');
    });

    Keyboard.addListener('keyboardDidHide', () => {
      console.log('keyboard did hide');
    });
  };

  const setupApp = async () => {
    if (!isNative) return;

    App.addListener('appStateChange', ({ isActive }) => {
      console.log('App state changed. Is active?', isActive);
    });

    App.addListener('backButton', () => {
      console.log('Back button pressed');
    });
  };

  const setupStatusBar = async () => {
    if (!isNative) return;

    await StatusBar.setStyle({ style: 'dark' });
    await StatusBar.setBackgroundColor({ color: '#ffffff' });
  };

  const initialize = async () => {
    if (!isNative) return;

    await setupPushNotifications();
    await setupKeyboard();
    await setupApp();
    await setupStatusBar();
  };

  return {
    isNative,
    takePicture,
    vibrate,
    storeData,
    getData,
    initialize
  };
};

export default useCapacitor;
